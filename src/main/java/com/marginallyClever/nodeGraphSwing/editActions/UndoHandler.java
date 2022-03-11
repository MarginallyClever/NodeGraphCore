package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphSwing.editActions.ActionRedo;
import com.marginallyClever.nodeGraphSwing.editActions.ActionUndo;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

public class UndoHandler implements UndoableEditListener {
    private final UndoManager undoManager;
    private final ActionUndo actionUndo;
    private final ActionRedo actionRedo;

    public UndoHandler(UndoManager undoManager, ActionUndo actionUndo, ActionRedo actionRedo) {
        super();
        this.undoManager = undoManager;
        this.actionUndo = actionUndo;
        this.actionRedo = actionRedo;
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        undoManager.addEdit(e.getEdit());
        actionUndo.update();
        actionRedo.update();
    }
}
