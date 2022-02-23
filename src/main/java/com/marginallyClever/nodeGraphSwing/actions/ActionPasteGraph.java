package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionPasteGraph extends AbstractAction implements EditAction {
    private final NodeGraphEditorPanel editor;

    public ActionPasteGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NodeGraph modelC = editor.getCopiedGraph().deepCopy();
        editor.getGraph().add(modelC);
        editor.setSelectedNodes(modelC.getNodes());
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getCopiedGraph().isEmpty());
    }
}
