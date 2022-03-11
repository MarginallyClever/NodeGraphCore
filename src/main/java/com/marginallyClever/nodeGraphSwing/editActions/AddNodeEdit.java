package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;

public class AddNodeEdit extends AbstractUndoableEdit {
    private final NodeGraphEditorPanel editor;
    private final Node node;

    public AddNodeEdit(NodeGraphEditorPanel editor, Node node) {
        super();
        this.editor = editor;
        this.node = node;
        doIt();
    }

    public void doIt() {
        editor.getGraph().add(node);
        editor.setSelectedNode(node);
        editor.repaint(node.getRectangle());
    }

    @Override
    public void undo() throws CannotUndoException {
        editor.getGraph().remove(node);

        List<Node> nodes = new ArrayList<>(editor.getSelectedNodes());
        nodes.remove(node);
        editor.setSelectedNodes(nodes);

        editor.repaint(node.getRectangle());

        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        doIt();
        super.redo();
    }
}
