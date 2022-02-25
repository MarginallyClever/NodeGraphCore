package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphCore.Subgraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Uncollapses all the editor's selected items that are {@link Subgraph}s.  The new list of selected items will contain
 * all the new graph elements plus any old elements that were not {@link Subgraph}s.
 * Each newly exposed graph is positioned relative to the original {@link Subgraph}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionUnfoldGraph extends AbstractAction implements EditAction {
    private final NodeGraphEditorPanel editor;

    public ActionUnfoldGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Node> wasSelected = editor.getSelectedNodes();
        ArrayList<Node> toBeDeleted = new ArrayList<>();
        List<Node> newSelection = editor.getSelectedNodes();

        for(Node n : wasSelected) {
            if (n instanceof Subgraph) {
                toBeDeleted.add(n);
            }
        }

        for(Node n : toBeDeleted) {
            NodeGraph inner = ((Subgraph)n).getGraph();
            // add the subgraph to this graph.
            editor.getGraph().add(inner);
            // make sure it is selected
            newSelection.addAll(inner.getNodes());
            // position it relative to the Subgraph it is replacing
            positionNodesRelativeTo(inner,n.getRectangle().x,n.getRectangle().y);
            // make sure to delete the Subgraph and clean up.
            editor.getGraph().remove(n);
            inner.clear();
        }

        // the list of selected nodes is all old nodes - subgraphs + newly expanded nodes.
        wasSelected.removeAll(toBeDeleted);
        newSelection.addAll(wasSelected);
        editor.setSelectedNodes(newSelection);
    }

    private void positionNodesRelativeTo(NodeGraph nodeGraph,int dx, int dy) {
        Rectangle r = nodeGraph.getBounds();
        dx-=r.x;
        dy-=r.y;
        for(Node n : nodeGraph.getNodes()) {
            n.moveRelative(dx,dy);
        }
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
