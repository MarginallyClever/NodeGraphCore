package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.EditAction;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Straightens the editor's {@link Node}s by rounding their top-left corner x and y values to the nearest
 * {@link ActionStraightenGraph#SNAP_SIZE}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionStraightenGraph extends AbstractAction implements EditAction {
    /**
     * The editor being affected.
     */
    private final NodeGraphEditorPanel editor;

    /**
     * The default snap rounding distance
     */
    private final int SNAP_SIZE = 10;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public ActionStraightenGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NodeGraph g = editor.getGraph();

        for(Node n : g.getNodes()) {
            Rectangle r = n.getRectangle();
            r.x -= r.x % SNAP_SIZE;
            r.y -= r.y % SNAP_SIZE;
        }
        editor.repaint();
    }

    @Override
    public void updateEnableStatus() {
        setEnabled(!editor.getSelectedNodes().isEmpty());
    }
}
