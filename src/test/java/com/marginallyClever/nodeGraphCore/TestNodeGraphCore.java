package com.marginallyClever.nodeGraphCore;

import com.google.gson.JsonElement;
import com.marginallyClever.nodeGraphCore.builtInNodes.LoadNumber;
import com.marginallyClever.nodeGraphCore.builtInNodes.PrintToStdOut;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.Add;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.Multiply;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.Subtract;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the NodeGraphCore elements.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class TestNodeGraphCore {
    private static NodeGraph model;

    @BeforeAll
    public static void beforeAll() {
        model = new NodeGraph();
        BuiltInNodeRegistry.registerNodes();
    }

    @BeforeEach
    public void beforeEach() {
        model.clear();
    }

    @Test
    public void testSaveEmptyGraph() {
        assertEquals("{\"nodes\":[],\"connections\":[]}",JSONHelper.getDefaultGson().toJson(model).replaceAll("\\s+",""));
    }

    @Test
    public void testAdd() {
        Node add = model.add(new Add());
        add.getVariable(0).setValue(1);
        add.getVariable(1).setValue(2);
        add.update();
        assertEquals( 3.0, add.getVariable(2).getValue() );
    }

    @Test
    public void testAddTwoConstants() {
        Node constant0 = model.add(new LoadNumber(1));
        Node constant1 = model.add(new LoadNumber(2));
        Node add = model.add(new Add());
        model.add(new NodeConnection(constant0,0,add,0));
        model.add(new NodeConnection(constant1,0,add,1));
        model.update();
        assertEquals( 3.0, add.getVariable(2).getValue() );
    }

    @Test
    public void testAddTwoConstantsAndReport() throws Exception {
        Node constant0 = model.add(new LoadNumber(1));
        Node constant1 = model.add(new LoadNumber(2));
        Node add = model.add(new Add());
        Node report = model.add(new PrintToStdOut());
        model.add(new NodeConnection(constant0,0,add,0));
        model.add(new NodeConnection(constant1,0,add,1));
        model.add(new NodeConnection(add,2,report,0));

        model.update();
        model.update();

        assertEquals( 3.0, report.getVariable(0).getValue() );
    }

    @Test
    public void testFactoryFailsOnBadRequests() {
        assertThrows(IllegalArgumentException.class, ()->{
            NodeFactory.createNode("");
        });

        assertThrows(IllegalArgumentException.class, ()->{
            NodeFactory.createNode(null);
        });
    }

    @Test
    public void testFactoryCreatesAllDefaultTypes() {
        assertNotEquals(0,NodeFactory.getNames().length);
        for(String s : NodeFactory.getNames()) {
            assertNotNull(NodeFactory.createNode(s));
        }
    }

    @Test
    public void testNodesAreNotEqual() {
        Node nodeA = new Add();
        Node nodeB = new Subtract();

        String asJsonA = JSONHelper.getDefaultGson().toJson(nodeA);
        String asJsonB = JSONHelper.getDefaultGson().toJson(nodeB);

        assertNotEquals(asJsonA, asJsonB);
        assertNotEquals(nodeA.toString(), nodeB.toString());
    }

    @Test
    public void testAllNodesToJSONAndBack() {
        for(String s : NodeFactory.getNames()) {
            Node a = NodeFactory.createNode(s);

            JsonElement element = JSONHelper.getDefaultGson().toJsonTree(a);
            Node b = JSONHelper.getDefaultGson().fromJson(element, Node.class);

            assertEquals(a.toString(),b.toString());
        }
    }

    @Test
    public void testModelToJSONAndBack() {
        testAddTwoConstants();
        JsonElement a = JSONHelper.getDefaultGson().toJsonTree(model);
        NodeGraph modelB = JSONHelper.getDefaultGson().fromJson(a, NodeGraph.class);
        assertEquals(model.toString(),modelB.toString());
    }

    @Test
    public void testModelClears() {
        testAddTwoConstants();
        model.clear();
        assertEquals(0,model.getNodes().size());
        assertEquals(0,model.getConnections().size());
    }

    @Test
    public void testFactoryCreatesAllSwingTypes() {
        assertNotEquals(0,NodeFactory.getNames().length);
        for(String s : NodeFactory.getNames()) {
            System.out.println(s);
            assertNotNull(NodeFactory.createNode(s));
        }
    }

    private <T> void testNodeVariableToJSONAndBack(Class<T> myClass,T instA,T instB) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        NodeVariable<?> a = NodeVariable.newInstance(myClass.getSimpleName(),myClass,instA,false,false);
        NodeVariable<?> b = NodeVariable.newInstance(myClass.getSimpleName(),myClass,instB,false,false);

        JSONHelper.deserializeNodeVariable(b, JSONHelper.serializeNodeVariable(a));
        assertEquals(a.toString(),b.toString());
        assertEquals(a.getValue(),b.getValue());
    }

    @Test
    public void testNodeVariablesToJSONAndBack() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        /* removed: it's not possible to serialize back to abstract classes
        testNodeVariableToJSONAndBack(Object.class, new Object(),new Object());
        testNodeVariableToJSONAndBack(Number.class, 1.2,0.0);
         */
        testNodeVariableToJSONAndBack(Rectangle.class, new Rectangle(),new Rectangle());
        testNodeVariableToJSONAndBack(String.class, "hello",new String());
        testNodeVariableToJSONAndBack(Double.class, 1.2D,0.0D);
        testNodeVariableToJSONAndBack(Integer.class, 1,0);
    }

    @Test
    public void testAddTwoModelsTogether() {
        testAddTwoConstants();

        NodeGraph modelB = new NodeGraph();
        modelB.add(model);
        modelB.add(model.deepCopy());

        assertEquals(2,modelB.countNodesOfClass(Add.class));
        assertEquals(4,modelB.countNodesOfClass(LoadNumber.class));

        // connect the Adds with a Multiply, update, and check the results.
        Node m = modelB.add(new Multiply());
        int a0index = modelB.indexOfNode(Add.class);
        int a1index = modelB.indexOfNode(Add.class,a0index+1);
        assertNotEquals(a0index,a1index);

        Node a0 = modelB.getNodes().get(a0index);
        Node a1 = modelB.getNodes().get(a1index);
        assertNotEquals(a0,a1);
        assertNotEquals(a0.getUniqueID(),a1.getUniqueID());

        modelB.add(new NodeConnection(a0,2,m,0));
        modelB.add(new NodeConnection(a1,2,m,1));
        modelB.update();
        assertEquals(9.0,m.getVariable(2).getValue());
    }
}
