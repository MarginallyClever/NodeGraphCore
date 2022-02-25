package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Updates all dirty {@link com.marginallyClever.nodeGraphCore.Node}s in the editor's graph.
 */
public class ActionUpdateGraph extends AbstractAction {
    private final NodeGraphEditorPanel editor;

    public ActionUpdateGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.update();
    }
}
