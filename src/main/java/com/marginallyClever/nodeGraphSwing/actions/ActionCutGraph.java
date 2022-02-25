package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphSwing.EditAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Performs an {@link ActionCopyGraph} and then an {@link ActionDeleteGraph}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionCutGraph extends AbstractAction implements EditAction {
    private final ActionDeleteGraph actionDeleteGraph;
    private final ActionCopyGraph actionCopyGraph;

    public ActionCutGraph(String name, ActionDeleteGraph actionDeleteGraph, ActionCopyGraph actionCopyGraph) {
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
