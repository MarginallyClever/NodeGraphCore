package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.port.Port;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;
import com.marginallyclever.nodegraphcore.nodes.LoadNumber;
import com.marginallyclever.nodegraphcore.nodes.PrintToStdOut;
import com.marginallyclever.nodegraphcore.nodes.math.Add;
import com.marginallyclever.nodegraphcore.nodes.math.Multiply;
import com.marginallyclever.nodegraphcore.nodes.math.Subtract;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the NodeGraphCore elements.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class TestGraph {
    private static final Graph graph = new Graph();

    @BeforeAll
    public static void beforeAll() throws Exception {
        NodeFactory.loadRegistries();
        BuiltInRegistry r = new BuiltInRegistry();
        r.registerDAO();
    }

    @AfterAll
    public static void afterAll() {
        NodeFactory.clear();
        DAO4JSONFactory.clear();
    }

    /**
     * clear the graph.
     */
    @BeforeEach
    public void beforeEach() {
        graph.clear();
    }

    /**
     * Test that empty graph look as expected.
     */
    @Test
    public void testSaveEmptyGraph() {
        var json = graph.toJSON();
        json.put("uniqueID","19781eb0-4009-4f5e-bc68-4e70de1b5181");
        assertEquals("{\"variables\":[],\"nodes\":[],\"name\":\"Graph\","
                +"\"rectangle\":{\"x\":0,\"width\":150,\"y\":0,\"height\":50},"
                +"\"label\":\"\",\"uniqueID\":\"19781eb0-4009-4f5e-bc68-4e70de1b5181\",\"connections\":[]}",
                json.toString());
    }

    private void buildAddTwoConstants() {
        LoadNumber constant0 = (LoadNumber) graph.add(new LoadNumber());
        LoadNumber constant1 = (LoadNumber) graph.add(new LoadNumber());
        constant0.getPort(0).setValue(1);
        constant1.getPort(0).setValue(2);
        Add add = (Add) graph.add(new Add());
        graph.add(new Connection(constant0,1,add,0));
        graph.add(new Connection(constant1,1,add,1));
    }

    /**
     * confirm adding two constants together via {@link Connection}s works as expected
     */
    @Test
    public void testAddTwoConstants() {
        buildAddTwoConstants();
        ThreadPoolScheduler scheduler = new ThreadPoolScheduler();
        scheduler.submit(graph.getNodes().get(0));
        scheduler.submit(graph.getNodes().get(1));
        scheduler.run();
        assertEquals( 3.0, graph.getNodes().get(2).getPort(2).getValue() );
    }

    /**
     * confirm adding two constants together via {@link Connection}s works as expected.
     * confirm {@link PrintToStdOut} works as expected.
     */
    @Test
    public void testAddTwoConstantsAndReport() {
        buildAddTwoConstants();
        ThreadPoolScheduler scheduler = new ThreadPoolScheduler();
        Node report = graph.add(new PrintToStdOut());
        graph.add(new Connection(graph.getNodes().get(2),2,report,0));

        scheduler.submit(graph.getNodes().get(0));
        scheduler.submit(graph.getNodes().get(1));
        scheduler.run();

        assertEquals( 3.0, report.getPort(0).getValue() );
    }

    /**
     * confirm factory will assert if asked for a node that doesn't exist.
     * confirm that there is no node with name "" or null.
     */
    @Test
    public void testFactoryFailsOnBadRequests() {
        assertThrows(IllegalArgumentException.class, ()-> NodeFactory.createNode(""));

        assertThrows(IllegalArgumentException.class, ()-> NodeFactory.createNode(null));
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
        Node nodeA = graph.add(new Add());
        Node nodeB = new Subtract();

        assertThrows(JSONException.class,()->nodeB.fromJSON(nodeA.toJSON()));
        assertNotEquals(nodeA.toString(), nodeB.toString());
        Node nodeC = graph.add(new Add());
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
            Node b = NodeFactory.createNode(s);
            assertNotNull(b);
            b.fromJSON(a.toJSON());
            assertEquals(a.toString(),b.toString());
        }
    }

    /**
     * confirm a {@link Graph} can be serialized and de-serialized.
     */
    @Test
    public void testGraphToJSONAndBack() {
        buildAddTwoConstants();
        JSONObject a = graph.toJSON();
        Graph modelB = new Graph();
        modelB.fromJSON(a);
        assertEquals(graph.toString(),modelB.toString());
    }

    /**
     * confirm clearing a {@link Graph} really does set it back to nothing.
     */
    @Test
    public void testGraphClears() {
        buildAddTwoConstants();
        graph.clear();
        assertEquals((new Graph()).toString(), graph.toString());
    }

    /**
     * confirm registering an already registered node does not throw an exception and does not double-register.
     */
    @Test
    public void testFactoryWontRegisterTwoNodesWithSameName() {
        assertThrows(GraphException.class, NodeFactory::loadRegistries);
    }

    /**
     * Test {@link Port} serialization
     * @param myClass
     * @param instA
     * @param instB
     * @param <T>
     * @throws Exception
     */
    private <T> void testNodeVariableToJSONAndBack(Class<T> myClass,T instA,T instB) {
        Port<?> a = new Input<>(myClass.getSimpleName(),myClass,instA);
        Port<?> b = new Input<>(myClass.getSimpleName(),myClass,instB);

        JSONObject obj = a.toJSON();
        b.fromJSON(obj);
        assertEquals(a.toString(),b.toString());
        assertEquals(a.getValue(),b.getValue());

        a = new Output<>(myClass.getSimpleName(),myClass,instA);
        b = new Output<>(myClass.getSimpleName(),myClass,instB);
        obj = a.toJSON();
        b.fromJSON(obj);
        assertEquals(a.toString(),b.toString());
        assertEquals(a.getValue(),b.getValue());
    }

    /**
     * Test {@link Port} serialization
     * @throws Exception if serialization fails.
     */
    @Test
    public void testNodeVariablesToJSONAndBack() {
        /* removed: it's not possible to serialize back to abstract classes
        testNodeVariableToJSONAndBack(Object.class, new Object(),new Object());
        testNodeVariableToJSONAndBack(Number.class, 1.2,0.0);
         */
        testNodeVariableToJSONAndBack(Rectangle.class, new Rectangle(),new Rectangle());
        testNodeVariableToJSONAndBack(String.class, "hello",new String());
        testNodeVariableToJSONAndBack(Number.class, 1.2D,0.0D);
        testNodeVariableToJSONAndBack(Number.class, 1,0);
    }

    /**
     * Test that two models can be added together
     * <ul>
     *     <li>without loss of {@link Node}s</li>
     *     <li>without loss of {@link Connection}</li>
     *     <li>while preserving values in the {@link Port}s</li>
     * </ul>
     */
    @Test
    public void testAddTwoGraphsTogether() {
        buildAddTwoConstants();

        ThreadPoolScheduler scheduler = new ThreadPoolScheduler();

        Graph modelB = new Graph();
        modelB.add(graph);
        modelB.add(graph.deepCopy());

        assertEquals(2,modelB.countNodesOfClass(Add.class));
        assertEquals(4,modelB.countNodesOfClass(LoadNumber.class));

        // connect the Adds with a Multiply, update, and check the results.
        int a0index = modelB.indexOfNode(Add.class);
        int a1index = modelB.indexOfNode(Add.class,a0index+1);
        assertNotEquals(a0index,a1index);

        Node a0 = modelB.getNodes().get(a0index);
        Node a1 = modelB.getNodes().get(a1index);
        assertNotEquals(a0,a1);
        assertNotEquals(a0.getUniqueID(),a1.getUniqueID());

        Node m = modelB.add(new Multiply());
        modelB.add(new Connection(a0,2,m,0));
        modelB.add(new Connection(a1,2,m,1));

        // TODO and input and output ports.

        // submit all the Adds to the scheduler
        int count = 0;
        for(Node n : modelB.getNodes()) {
            if(n instanceof LoadNumber) {
                scheduler.submit(n);
                count++;
            }
        }
        assertEquals(4,count);

        scheduler.run();

        assertEquals(9.0,m.getPort(2).getValue());
    }
}
