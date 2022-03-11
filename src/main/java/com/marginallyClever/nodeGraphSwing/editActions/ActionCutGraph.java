package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphSwing.EditAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Performs an {@link ActionCopyGraph} and then an {@link DeleteGraphAction}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionCutGraph extends AbstractAction implements EditAction {
    /**
     * The delete action on which this action depends.
     */
    private final DeleteGraphAction actionDeleteGraph;
    /**
     * The copy action on which this action depends.
     */
    private final ActionCopyGraph actionCopyGraph;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param actionDeleteGraph the delete action to use
     * @param actionCopyGraph the copy action to use
     */
    public ActionCutGraph(String name, DeleteGraphAction actionDeleteGraph, ActionCopyGraph actionCopyGraph) {
        super(name);
        this.actionDeleteGraph = actionDeleteGraph;
        this.actionCopyGraph = actionCopyGraph;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionCopyGraph.actionPerformed(e);
        actionDeleteGraph.actionPerformed(e);
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(actionDeleteGraph.isEnabled());
    }
}
