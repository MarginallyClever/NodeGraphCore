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
    private static NodeGraph nodeGraph;

    /**
     * setup the {@link NodeGraph} to use.
     */
    @BeforeAll
    public static void beforeAll() {
        nodeGraph = new NodeGraph();
        BuiltInNodeRegistry.registerNodes();
    }

    /**
     * clear the graph.
     */
    @BeforeEach
    public void beforeEach() {
        nodeGraph.clear();
    }

    /**
     * Test that empty graph look as expected.
     */
    @Test
    public void testSaveEmptyGraph() {
        assertEquals("{\"nodes\":[],\"connections\":[]}",JSONHelper.getDefaultGson().toJson(nodeGraph).replaceAll("\\s+",""));
    }

    /**
     * confirm {@link Add#update()} works as expected and sets itself to not dirty
     */
    @Test
    public void testAdd() {
        Node add = nodeGraph.add(new Add());
        add.getVariable(0).setValue(1);
        add.getVariable(1).setValue(2);
        add.update();
        assertEquals( 3.0, add.getVariable(2).getValue() );
        assertEquals( false, add.isDirty() );
    }

    /**
     * confirm adding two constants together via {@link NodeConnection}s works as expected
     */
    @Test
    public void testAddTwoConstants() {
        Node constant0 = nodeGraph.add(new LoadNumber(1));
        Node constant1 = nodeGraph.add(new LoadNumber(2));
        Node add = nodeGraph.add(new Add());
        nodeGraph.add(new NodeConnection(constant0,0,add,0));
        nodeGraph.add(new NodeConnection(constant1,0,add,1));
        nodeGraph.update();
        assertEquals( 3.0, add.getVariable(2).getValue() );
    }

    /**
     * confirm adding two constants together via {@link NodeConnection}s works as expected.
     * confirm {@link PrintToStdOut} works as expected.
     */
    @Test
    public void testAddTwoConstantsAndReport() {
        Node constant0 = nodeGraph.add(new LoadNumber(1));
        Node constant1 = nodeGraph.add(new LoadNumber(2));
        Node add = nodeGraph.add(new Add());
        Node report = nodeGraph.add(new PrintToStdOut());
        nodeGraph.add(new NodeConnection(constant0,0,add,0));
        nodeGraph.add(new NodeConnection(constant1,0,add,1));
        nodeGraph.add(new NodeConnection(add,2,report,0));

        nodeGraph.update();
        nodeGraph.update();

        assertEquals( 3.0, report.getVariable(0).getValue() );
    }

    /**
     * confirm factory will assert if asked for a node that doesn't exist.
     * confirm that there is no node with name "" or null.
     */
    @Test
    public void testFactoryFailsOnBadRequests() {
        assertThrows(IllegalArgumentException.class, ()->{
            NodeFactory.createNode("");
        });

        assertThrows(IllegalArgumentException.class, ()->{
            NodeFactory.createNode(null);
        });
    }

    /**
     * confirm factory can create all registered nodes.
     */
    @Test
    public void testFactoryCreatesAllDefaultTypes() {
        assertNotEquals(0,NodeFactory.getNames().length);
        for(String s : NodeFactory.getNames()) {
            assertNotNull(NodeFactory.createNode(s));
        }
    }

    /**
     * confirm two different nodes are not somehow equal when serialized.
     * confirm two nodes of the same type in the same graph are not equal when serialized.
     */
    @Test
    public void testNodesAreNotEqual() {
        Node nodeA = nodeGraph.add(new Add());
        Node nodeB = new Subtract();

        String asJsonA = JSONHelper.getDefaultGson().toJson(nodeA);
        String asJsonB = JSONHelper.getDefaultGson().toJson(nodeB);

        assertNotEquals(asJsonA, asJsonB);
        assertNotEquals(nodeA.toString(), nodeB.toString());

        Node nodeC = nodeGraph.add(new Add());
        String asJsonC = JSONHelper.getDefaultGson().toJson(nodeC);
        assertNotEquals(nodeA.toString(), nodeC.toString());
    }

    /**
     * confirm all {@link Node}s can be created by the {@link NodeFactory}.
     * confirm all {@link Node}s can be serialized and de-serialized.
     * confirm all {@link Node}s are identical after being serialized and de-serialized.
     */
    @Test
    public void testAllNodesToJSONAndBack() {
        for(String s : NodeFactory.getNames()) {
            System.out.println(s);
            Node a = NodeFactory.createNode(s);
            assertNotNull(a);

            JsonElement element = JSONHelper.getDefaultGson().toJsonTree(a);
            Node b = JSONHelper.getDefaultGson().fromJson(element, Node.class);

            assertEquals(a.toString(),b.toString());
        }
    }

    /**
     * confirm a {@link NodeGraph} can be serialized and de-serialized.
     */
    @Test
    public void testModelToJSONAndBack() {
        testAddTwoConstants();
        JsonElement a = JSONHelper.getDefaultGson().toJsonTree(nodeGraph);
        NodeGraph modelB = JSONHelper.getDefaultGson().fromJson(a, NodeGraph.class);
        assertEquals(nodeGraph.toString(),modelB.toString());
    }

    /**
     * confirm clearing a {@link NodeGraph} really does set it back to nothing.
     */
    @Test
    public void testModelClears() {
        testAddTwoConstants();
        nodeGraph.clear();
        assertEquals((new NodeGraph()).toString(),nodeGraph.toString());
    }

    /**
     * confirm registering an already registered node throws an exception.
     */
    @Test
    public void testFactoryWontRegisterTwoNodesWithSameName() {
        assertThrows(IllegalArgumentException.class,()->NodeFactory.registerNode(new Add()));
    }

    /**
     * Test {@link NodeVariable} serialization
     * @param myClass
     * @param instA
     * @param instB
     * @param <T>
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private <T> void testNodeVariableToJSONAndBack(Class<T> myClass,T instA,T instB) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        NodeVariable<?> a = NodeVariable.newInstance(myClass.getSimpleName(),myClass,instA,false,false);
        NodeVariable<?> b = NodeVariable.newInstance(myClass.getSimpleName(),myClass,instB,false,false);

        JSONHelper.deserializeNodeVariable(b, JSONHelper.serializeNodeVariable(a));
        assertEquals(a.toString(),b.toString());
        assertEquals(a.getValue(),b.getValue());
    }

    /**
     * Test {@link NodeVariable} serialization
     * @throws Exception if serialization fails.
     */
    @Test
    public void testNodeVariablesToJSONAndBack() throws Exception {
        /* removed: it's not possible to serialize back to abstract classes
        testNodeVariableToJSONAndBack(Object.class, new Object(),new Object());
        testNodeVariableToJSONAndBack(Number.class, 1.2,0.0);
         */
        testNodeVariableToJSONAndBack(Rectangle.class, new Rectangle(),new Rectangle());
        testNodeVariableToJSONAndBack(String.class, "hello",new String());
        testNodeVariableToJSONAndBack(Double.class, 1.2D,0.0D);
        testNodeVariableToJSONAndBack(Integer.class, 1,0);
    }

    /**
     * Test that two models can be added together
     * <ul>
     *     <li>without loss of {@link Node}s</li>
     *     <li>without loss of {@link NodeConnection}</li>
     *     <li>while preserving values in the {@link NodeVariable}s</li>
     * </ul>
     */
    @Test
    public void testAddTwoModelsTogether() {
        testAddTwoConstants();

        NodeGraph modelB = new NodeGraph();
        modelB.add(nodeGraph);
        modelB.add(nodeGraph.deepCopy());

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
