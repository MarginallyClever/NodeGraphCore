package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Clears the editor's current {@link com.marginallyClever.nodeGraphCore.NodeGraph}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionNewGraph extends AbstractAction {
    private final NodeGraphEditorPanel editor;

    public ActionNewGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.clear();
    }
}
