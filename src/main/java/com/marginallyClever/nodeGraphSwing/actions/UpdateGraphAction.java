package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphSwing.Donatello;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Updates all dirty {@link com.marginallyClever.nodeGraphCore.Node}s in the editor's graph.  It cannot be undone.
 */
public class UpdateGraphAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public UpdateGraphAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    /**
     * Updates the model and repaints the panel.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            editor.getGraph().update();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        editor.repaint();
    }
}
