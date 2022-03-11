package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;

public class DeleteGraphEdit extends AbstractUndoableEdit {
    private final NodeGraphEditorPanel editor;
    private final List<Node> nodes = new ArrayList<>();
    private final List<NodeConnection> connections = new ArrayList<>();

    public DeleteGraphEdit(NodeGraphEditorPanel editor, List<Node> selectedNodes) {
        super();
        this.editor = editor;
        this.nodes.addAll(selectedNodes);
        connections.addAll(editor.getGraph().getExteriorConnections(selectedNodes));
        connections.addAll(editor.getGraph().getInteriorConnections(selectedNodes));
        doIt();
    }

    private void doIt() {
        NodeGraph g = editor.getGraph();
        for(Node n : nodes) g.remove(n);
        for(NodeConnection c : connections) g.remove(c);
        editor.setSelectedNodes(null);
        editor.repaint();
    }

    @Override
    public void undo() throws CannotUndoException {
        NodeGraph g = editor.getGraph();
        for(Node n : nodes) g.add(n);
        for(NodeConnection c : connections) g.add(c);
        editor.setSelectedNodes(nodes);
        editor.repaint();
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
