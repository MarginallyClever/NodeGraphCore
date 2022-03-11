package com.marginallyClever.nodeGraphSwing.editActions.undoable;

import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class RemoveConnectionEdit extends AbstractUndoableEdit {
    private final String name;
    private final NodeGraphEditorPanel editor;
    private final NodeConnection connection;

    public RemoveConnectionEdit(String name, NodeGraphEditorPanel editor, NodeConnection connection) {
        super();
        this.name = name;
        this.editor = editor;
        this.connection = connection;
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.getGraph().add(connection);
        super.undo();
    }

    private void doIt() {
        editor.getGraph().remove(connection);
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
