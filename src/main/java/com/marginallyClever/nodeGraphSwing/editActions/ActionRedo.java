package com.marginallyClever.nodeGraphSwing.editActions;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class ActionRedo extends AbstractAction {
    private final UndoManager undoManager;
    private ActionUndo actionUndo;

    public ActionRedo(UndoManager undoManager) {
        super("Redo");
        setEnabled(false);
        this.undoManager = undoManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            undoManager.redo();
        }
        catch (CannotRedoException ex) {
            // TODO deal with this
            ex.printStackTrace();
        }
        update();
        actionUndo.update();
    }

    protected void update() {
        if (undoManager.canRedo()) {
            setEnabled(true);
            putValue(Action.NAME, undoManager.getRedoPresentationName());
        } else {
            setEnabled(false);
            putValue(Action.NAME, "Redo");
        }
    }

    public void setActionUndo(ActionUndo actionUndo) {
        this.actionUndo = actionUndo;
    }
}
