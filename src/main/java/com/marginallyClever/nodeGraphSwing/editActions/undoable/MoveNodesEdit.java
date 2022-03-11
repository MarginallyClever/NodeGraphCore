package com.marginallyClever.nodeGraphSwing.editActions.undoable;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by the {@link com.marginallyClever.nodeGraphSwing.modalTools.NodeMoveTool} so that reorganizations
 * can be undone.  This edit is created when a move is complete and should record only the significant relative change
 * in position.  Since the tool already completed the move there is no need to apply the move in the constructor.
 * @author Dan Royer
 * @since 2022-03-11
 */
public class MoveNodesEdit extends AbstractUndoableEdit {
    private final String name;
    private final NodeGraphEditorPanel editor;
    private final int dx,dy;
    private final List<Node> selected = new ArrayList<>();

    public MoveNodesEdit(String name, NodeGraphEditorPanel editor,int dx,int dy) {
        super();
        this.name = name;
        this.editor = editor;
        this.dx = dx;
        this.dy = dy;
        this.selected.addAll(editor.getSelectedNodes());
    }

    @Override
    public String getPresentationName() {
        return name;
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.setSelectedNodes(selected);
        editor.moveSelectedNodes(-dx,-dy);
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        editor.setSelectedNodes(selected);
        editor.moveSelectedNodes(dx,dy);
        super.redo();
    }
}
