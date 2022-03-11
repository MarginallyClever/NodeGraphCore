package com.marginallyClever.nodeGraphSwing.editActions.undoable;

import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.List;

/**
 * Adds a {@link NodeConnection} to a {@link com.marginallyClever.nodeGraphCore.NodeVariable}.
 * Since the inbound node can only have one connection at a time, this edit also preserves any connection that has
 * to be removed.
 */
public class AddConnectionEdit extends AbstractUndoableEdit {
    private final String name;
    private final NodeGraphEditorPanel editor;
    private final NodeConnection connection;
    private final List<NodeConnection> connectionsInto;

    public AddConnectionEdit(String name, NodeGraphEditorPanel editor, NodeConnection connection) {
        super();
        this.name = name;
        this.editor = editor;
        this.connection = connection;
        this.connectionsInto = editor.getGraph().getAllConnectionsInto(connection.getOutVariable());
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    private void doIt() {
        NodeGraph graph = editor.getGraph();
        for(NodeConnection c : connectionsInto) {
            graph.remove(c);
        }
        graph.add(connection);
    }

    @Override
    public void undo() throws CannotUndoException {
        NodeGraph graph = editor.getGraph();
        graph.remove(connection);
        for(NodeConnection c : connectionsInto) {
            graph.add(c);
        }
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
