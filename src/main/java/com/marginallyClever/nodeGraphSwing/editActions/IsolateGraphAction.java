package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Disconnects the selected {@link Node}s from any non-selected {@link Node}s of a {@link NodeGraph}.
 * @author Dan Royer
 * @since 2022-03-10
 */
public class IsolateGraphAction extends AbstractAction implements EditAction {
    /**
     * The editor being affected.
     */
    private final NodeGraphEditorPanel editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public IsolateGraphAction(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.addEdit(new IsolateGraphEdit(editor,editor.getSelectedNodes()));
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}