package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Updates all dirty nodes in the graph
 */
public class ActionUpdateGraph extends AbstractAction {
    NodeGraphEditorPanel editor;

    public ActionUpdateGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.update();
    }
}
