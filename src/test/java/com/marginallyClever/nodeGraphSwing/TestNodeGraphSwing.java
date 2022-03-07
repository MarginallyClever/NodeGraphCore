package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.*;
import com.marginallyClever.nodeGraphSwing.nodes.images.LoadImage;
import com.marginallyClever.nodeGraphSwing.nodes.images.PrintImage;
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
    public static void beforeAll() {
        model = new NodeGraph();
        try {
            BuiltInNodeRegistry.registerNodes();
        } catch (IllegalArgumentException e) {}
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

    @Test
    public void testImages() {
        LoadImage img2 = new LoadImage("doesNotExist.png");
        img2.update();

        LoadImage img = new LoadImage("src/test/resources/test.png");
        img.update();

        PrintImage printer = new PrintImage();
        NodeConnection c = new NodeConnection(img,0,printer,0);
    }
}
