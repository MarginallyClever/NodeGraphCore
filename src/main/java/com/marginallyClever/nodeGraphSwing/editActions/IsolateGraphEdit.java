package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IsolateGraphEdit extends AbstractUndoableEdit {
    private final NodeGraphEditorPanel editor;
    private final List<NodeConnection> connections = new ArrayList<>();

    public IsolateGraphEdit(NodeGraphEditorPanel editor, List<Node> selectedNodes) {
        super();
        this.editor = editor;
        connections.addAll(editor.getGraph().getExteriorConnections(selectedNodes));
        doIt();
    }

    private void doIt() {
        NodeGraph graph = editor.getGraph();
        graph.getConnections().removeAll(connections);
        editor.repaint();
    }

    @Override
    public void undo() throws CannotUndoException {
        NodeGraph g = editor.getGraph();
        for(NodeConnection c : connections) g.add(c);
        editor.repaint();
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
