package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestNodeGraphSwing {
    private static NodeGraph model;

    @BeforeAll
    static void beforeAll() {
        model = new NodeGraph();
        NodeFactory.registerBuiltInNodes();
    }

    @BeforeEach
    public void beforeEach() {
        model.clear();
    }
}
