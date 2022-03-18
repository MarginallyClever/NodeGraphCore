package com.marginallyclever.donatello.edits;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.donatello.Donatello;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.List;

public class AddNodeEdit extends SignificantUndoableEdit {
    private final String name;
    private final Donatello editor;
    private final Node node;

    public AddNodeEdit(String name, Donatello editor, Node node) {
        super();
        this.name = name;
        this.editor = editor;
        this.node = node;
        doIt();
    }

    @Override
    public String getPresentationName() {
        return name;
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
