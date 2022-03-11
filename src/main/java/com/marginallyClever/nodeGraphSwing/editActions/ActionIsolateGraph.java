package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Disconnects the selected {@link Node}s from any non-selected {@link Node}s of a {@link NodeGraph}.
 * @author Dan Royer
 * @since 2022-03-10
 */
public class ActionIsolateGraph extends AbstractAction implements EditAction {
    /**
     * The editor being affected.
     */
    private final NodeGraphEditorPanel editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public ActionIsolateGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NodeGraph graph = editor.getGraph();

        List<Node> selectedNodes = editor.getSelectedNodes();
        List<NodeConnection> toSever = getOutsideConnections(graph,selectedNodes);
        graph.getConnections().removeAll(toSever);

        repaintAsNeeded(toSever);
    }

    private void repaintAsNeeded(List<NodeConnection> toSever) {
        if(!toSever.isEmpty()) {
            Rectangle r = new Rectangle(toSever.get(0).getBounds());
            for(NodeConnection c : toSever) r.add(c.getBounds());
            editor.repaint(r);
        }
    }

    /**
     * Returns a list of connections in the given {@link NodeGraph} that connect to the selected {@link Node}s and unselected {@link Node}s.
     * @param graph the {@link NodeGraph} containing selected nodes
     * @param selectedNodes the subset of {@link Node}s.
     * @return a list of connections in the given {@link NodeGraph} that connect to the selected {@link Node}s and unselected {@link Node}s.
     */
    private List<NodeConnection> getOutsideConnections(NodeGraph graph, List<Node> selectedNodes) {
        ArrayList<NodeConnection> found = new ArrayList<>();

        for(NodeConnection c : graph.getConnections()) {
            int hits=0;
            for(Node n : selectedNodes) {
                if(c.isConnectedTo(n)) {
                    hits++;
                    if(hits==2) break;
                }
            }
            if(hits==1) {
                found.add(c);
            }
        }
        return found;
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
