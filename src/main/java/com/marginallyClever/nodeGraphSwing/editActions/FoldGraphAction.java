package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphCore.Subgraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;
import com.marginallyClever.nodeGraphSwing.editActions.undoable.CutGraphAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Collapses the editor's selected {@link Node}s into a new sub-graph.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class FoldGraphAction extends AbstractAction implements EditAction {
    /**
     * The editor being affected.
     */
    private final NodeGraphEditorPanel editor;
    /**
     * The cut action on which this action depends.
     */
    private final CutGraphAction cutGraphAction;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     * @param CutGraphAction the cut action to use with this Action.
     */
    public FoldGraphAction(String name, NodeGraphEditorPanel editor, CutGraphAction CutGraphAction) {
        super(name);
        this.editor = editor;
        this.cutGraphAction = CutGraphAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NodeGraph preserveCopyBehaviour = editor.getCopiedGraph().deepCopy();

        cutGraphAction.actionPerformed(e);
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
