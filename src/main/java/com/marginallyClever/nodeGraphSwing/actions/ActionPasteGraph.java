package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Duplicates the editor's copy buffer, inserts the contents into the editor's current {@link NodeGraph}, and sets the
 * new content as the editor's selected items.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionPasteGraph extends AbstractAction implements EditAction {
    /**
     * The editor being affected.
     */
    private final NodeGraphEditorPanel editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public ActionPasteGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NodeGraph modelC = editor.getCopiedGraph().deepCopy();
        editor.getGraph().add(modelC);
        editor.setSelectedNodes(modelC.getNodes());
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getCopiedGraph().isEmpty());
    }
}
