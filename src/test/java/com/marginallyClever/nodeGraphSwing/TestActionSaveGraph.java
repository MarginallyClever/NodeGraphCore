package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphSwing.actions.ActionSaveGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestActionSaveGraph {
    @Test
    public void testAddExtension() {
        ActionSaveGraph actionSaveGraph = new ActionSaveGraph("Save",null);
        assertEquals("test.graph",actionSaveGraph.addExtensionIfNeeded("test"));
        assertEquals("test.graph",actionSaveGraph.addExtensionIfNeeded("test.graph"));
    }
}
