package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphCore.Subgraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionFoldGraph extends AbstractAction implements EditAction {
    private NodeGraphEditorPanel editor;
    private ActionCutGraph actionCutGraph;

    public ActionFoldGraph(String name, NodeGraphEditorPanel editor, ActionCutGraph actionCutGraph) {
        super(name);
        this.editor = editor;
        this.actionCutGraph = actionCutGraph;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NodeGraph preserveCopyBehaviour = editor.getCopiedGraph().deepCopy();

        actionCutGraph.actionPerformed(e);
        NodeGraph justCut = editor.getCopiedGraph().deepCopy();
        Node n = editor.getGraph().add(new Subgraph(justCut));
        n.setPosition(editor.getPopupPoint());

        editor.setCopiedGraph(preserveCopyBehaviour);
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
