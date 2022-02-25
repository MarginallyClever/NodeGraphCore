package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphCore.Subgraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Collapses the editor's selected {@link Node}s into a new sub-graph.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionFoldGraph extends AbstractAction implements EditAction {
    /**
     * The editor being affected.
     */
    private NodeGraphEditorPanel editor;
    /**
     * The cut action on which this action depends.
     */
    private ActionCutGraph actionCutGraph;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     * @param actionCutGraph the cut action to use with this Action.
     */
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
