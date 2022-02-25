package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test the NodeGraphSwing elements.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class TestNodeGraphSwing {
    private static NodeGraph model;

    @BeforeAll
    static void beforeAll() {
        model = new NodeGraph();
        BuiltInNodeRegistry.registerNodes();
        SwingNodeRegistry.registerNodes();
    }

    /**
     * Reset
     */
    @BeforeEach
    public void beforeEach() {
        model.clear();
    }

    /**
     * Make sure all nodes introduced in this package can be created.
     */
    @Test
    public void testFactoryCreatesAllSwingTypes() {
        assertNotEquals(0,NodeFactory.getNames().length);
        for(String s : NodeFactory.getNames()) {
            System.out.println(s);
            assertNotNull(NodeFactory.createNode(s));
        }
    }
}
