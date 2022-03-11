package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Copies the editor's selected {@link Node}s and their shared {@link NodeConnection}s to the copy buffer.  The copy
 * buffer ensures the items so that they can be pasted even after they are deleted.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class CopyGraphAction extends AbstractAction implements EditAction {
    /**
     * The editor being affected.
     */
    private final NodeGraphEditorPanel editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public CopyGraphAction(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NodeGraph graph = editor.getGraph();

        NodeGraph modelB = new NodeGraph();
        List<Node> selectedNodes = editor.getSelectedNodes();
        for(Node n : selectedNodes) modelB.add(n);
        List<NodeConnection> selectedConnections = getConnectionsBetweenTheseNodes(graph,selectedNodes);
        for(NodeConnection c : selectedConnections) modelB.add(c);
        editor.setCopiedGraph(modelB.deepCopy());
    }

    /**
     * Returns all {@link NodeConnection}s that are only connected between the given set of nodes.
     * @param selectedNodes the set of nodes to check
     * @return all {@link NodeConnection}s that are only connected between the given set of nodes.
     */
    private List<NodeConnection> getConnectionsBetweenTheseNodes(NodeGraph graph,List<Node> selectedNodes) {
        List<NodeConnection> list = new ArrayList<>();
        if(selectedNodes.size()<2) return list;

        for(NodeConnection c : graph.getConnections()) {
            if(selectedNodes.contains(c.getOutNode()) && selectedNodes.contains(c.getInNode())) {
                list.add(c);
            }
        }
        return list;
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
