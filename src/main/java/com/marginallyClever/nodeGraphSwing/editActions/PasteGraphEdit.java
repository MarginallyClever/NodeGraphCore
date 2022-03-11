package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import java.awt.*;

public class PasteGraphEdit extends AbstractUndoableEdit {
    private final NodeGraphEditorPanel editor;
    private final NodeGraph copiedGraph;
    private final Point m;

    public PasteGraphEdit(NodeGraphEditorPanel editor, NodeGraph graph) {
        super();
        this.editor = editor;
        this.copiedGraph = graph.deepCopy();
        this.m = editor.getMousePosition();
        doIt();
    }

    private void doIt() {
        editor.getGraph().add(copiedGraph);
        editor.setSelectedNodes(copiedGraph.getNodes());
        copiedGraph.updateBounds();
        Rectangle r = copiedGraph.getBounds();
        editor.moveSelectedNodes(m.x-r.x,m.y-r.y);
        editor.repaint();
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.getGraph().remove(copiedGraph);
        editor.setSelectedNodes(null);
        editor.repaint();
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
