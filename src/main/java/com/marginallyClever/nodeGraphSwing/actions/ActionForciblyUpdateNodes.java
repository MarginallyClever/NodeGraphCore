package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionForciblyUpdateNodes extends AbstractAction {
    private final NodeGraphEditorPanel editor;

    public ActionForciblyUpdateNodes(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Node n : editor.getSelectedNodes()) {
            n.update();
        }
    }
}
