package com.marginallyClever.nodeGraphSwing.actions.undoable;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.Donatello;
import com.marginallyClever.nodeGraphSwing.actions.EditorAction;
import com.marginallyClever.nodeGraphSwing.edits.IsolateGraphEdit;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Disconnects the selected {@link Node}s from any non-selected {@link Node}s of a {@link NodeGraph}.
 * @author Dan Royer
 * @since 2022-03-10
 */
public class IsolateGraphAction extends AbstractAction implements EditorAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public IsolateGraphAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.addEdit(new IsolateGraphEdit((String)this.getValue(Action.NAME),editor,editor.getSelectedNodes()));
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
