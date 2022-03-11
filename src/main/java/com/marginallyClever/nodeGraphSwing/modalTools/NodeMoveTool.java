package com.marginallyClever.nodeGraphSwing.modalTools;

import com.marginallyClever.nodeGraphSwing.ModalTool;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;
import com.marginallyClever.nodeGraphSwing.NodeGraphViewPanel;
import com.marginallyClever.nodeGraphSwing.editActions.undoable.MoveNodesEdit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class NodeMoveTool extends ModalTool {
    private final NodeGraphEditorPanel editor;

    /**
     * true while dragging one or more nodes around.
     */
    private boolean dragOn=false;

    /**
     * for tracking relative motion, useful for relative moves like dragging.
     */
    private final Point mousePreviousPosition = new Point();

    /**
     * for tracking total motion, useful for undoable edit.
     */
    private final Point mouseStartPosition = new Point();

    public NodeMoveTool(NodeGraphEditorPanel editor) {
        super();
        this.editor = editor;
    }

    @Override
    public void paint(Graphics g) {
        // TODO draw rectangle around selected nodes?
        // TODO draw start of drag point to current point?
    }

    @Override
    public String getName() {
        return "Move";
    }

    public KeyStroke getAcceleratorKey() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_M,0);
    }

    @Override
    public void attachMouseAdapter() {
        super.attachMouseAdapter();
        NodeGraphViewPanel paintArea = editor.getPaintArea();
        paintArea.addMouseMotionListener(this);
        paintArea.addMouseListener(this);
    }

    @Override
    public void detachMouseAdapter() {
        super.detachMouseAdapter();
        NodeGraphViewPanel paintArea = editor.getPaintArea();
        paintArea.removeMouseMotionListener(this);
        paintArea.removeMouseListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(dragOn) {
            int dx = e.getX() - mousePreviousPosition.x;
            int dy = e.getY() - mousePreviousPosition.y;
            editor.moveSelectedNodes(dx, dy);
            editor.repaint();
        }
        mousePreviousPosition.setLocation(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePreviousPosition.setLocation(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!dragOn) {
            dragOn=true;
            mouseStartPosition.setLocation(e.getX(),e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(dragOn) {
            dragOn=false;
            int dx = e.getX() - mouseStartPosition.x;
            int dy = e.getY() - mouseStartPosition.y;
            editor.addEdit(new MoveNodesEdit(getName(),editor,dx,dy));
        }
    }
}
