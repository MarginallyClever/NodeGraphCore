package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionEditNodes extends AbstractAction {
    private final NodeGraphEditorPanel editor;

    public ActionEditNodes(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Edit node(s)");
        throw new RuntimeException("Not implemented");
    }
}
