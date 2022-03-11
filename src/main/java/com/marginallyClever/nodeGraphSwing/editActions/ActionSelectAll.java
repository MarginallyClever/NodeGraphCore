package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Select all the {@link com.marginallyClever.nodeGraphCore.Node}s in the {@link com.marginallyClever.nodeGraphCore.NodeGraph}.
 */
public class ActionSelectAll extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final NodeGraphEditorPanel editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public ActionSelectAll(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    /**
     * Updates the model and repaints the panel.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        editor.setSelectedNodes(editor.getGraph().getNodes());
        editor.repaint();
    }
}
