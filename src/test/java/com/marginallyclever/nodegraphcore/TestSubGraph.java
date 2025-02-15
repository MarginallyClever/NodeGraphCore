package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.port.Port;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;
import com.marginallyclever.nodegraphcore.nodes.math.Add;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

public class TestSubGraph {
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

    @Test
    public void test() throws Exception {
        Graph graph = new Graph();
        Add add = new Add();
        graph.add(add);
        Subgraph subgraph = new Subgraph();
        subgraph.setGraph(graph);
        // cannot check because deep copy changes unique IDs
        //Assertions.assertEquals(graph.toString(), subgraph.getGraph().toString());
        Assertions.assertEquals(0,subgraph.getPorts().size());

        subgraph.update();

        subgraph.setGraph(null);
        // cannot check because deep copy changes unique IDs
        //Assertions.assertEquals(new Graph(),subgraph.getGraph());
        Assertions.assertEquals(0,subgraph.getPorts().size());
    }

    @Test
    public void testGraphics() {
        Subgraph subgraph = new Subgraph();
        Graphics graphics = new DebugGraphics();
        subgraph.print(graphics);
    }

    @Test
    public void testVariablePair() {
        Port<?> port = new Output("a",Integer.class,0);
        Subgraph.VariablePair pair = new Subgraph.VariablePair(port);
        Assertions.assertEquals(port,pair.subVariable);
        Assertions.assertInstanceOf(Input.class,pair.superVariable);
    }
}
