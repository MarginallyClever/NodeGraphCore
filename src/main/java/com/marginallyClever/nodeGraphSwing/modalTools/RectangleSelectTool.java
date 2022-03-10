package com.marginallyClever.nodeGraphSwing.modalTools;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphSwing.KeyStateMemory;
import com.marginallyClever.nodeGraphSwing.ModalTool;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;
import com.marginallyClever.nodeGraphSwing.NodeGraphViewPanel;
import com.marginallyClever.nodeGraphSwing.editActions.ActionKeyStateMemory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

public class RectangleSelectTool extends ModalTool {
    private final NodeGraphEditorPanel editor;

    /**
     * true while drawing a box to select nodes.
     */
    private boolean selectionOn=false;

    /**
     * first corner of the bounding area when a user clicks and drags to form a box.
     */
    private final Point selectionAreaStart = new Point();

    /**
     * for tracking relative motion, useful for relative moves like dragging.
     */
    private final Point mousePreviousPosition = new Point();

    /**
     * Remembers the state of the keys for selection actions
     */
    private final KeyStateMemory keyStateMemory = new KeyStateMemory();

    public RectangleSelectTool(NodeGraphEditorPanel editor) {
        super();
        this.editor=editor;
    }

    @Override
    public String getName() {
        return "Select";
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
        paintArea.addMouseMotionListener(null);
        paintArea.addMouseListener(null);
    }

    @Override
    public void detachKeyboardAdapter() {
        super.detachKeyboardAdapter();
        NodeGraphViewPanel paintArea = editor.getPaintArea();
        paintArea.getActionMap().put("press",null);
        paintArea.getActionMap().put("release",null);
    }

    @Override
    public void attachKeyboardAdapter() {
        super.attachKeyboardAdapter();
        NodeGraphViewPanel paintArea = editor.getPaintArea();

        paintArea.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK,false),"press");
        paintArea.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0,true),"release");

        paintArea.getActionMap().put("press",new ActionKeyStateMemory(keyStateMemory, KeyEvent.VK_SHIFT,false));
        paintArea.getActionMap().put("release",new ActionKeyStateMemory(keyStateMemory, KeyEvent.VK_SHIFT,true));
    }

    @Override
    public void paint(Graphics g) {
        if(selectionOn) paintSelectionArea(g);
    }

    /**
     * Paints the rectangle selection area.
     * @param g the {@link Graphics} context
     */
    private void paintSelectionArea(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                2, new float[]{3}, 0);
        g2d.setStroke(dashed);

        g2d.setColor(keyStateMemory.isShiftKeyDown() ? Color.YELLOW : Color.MAGENTA);
        Rectangle2D r = getSelectionArea(mousePreviousPosition);
        g2d.drawRect((int)r.getMinX(),(int)r.getMinY(),(int)r.getWidth(),(int)r.getHeight());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        editor.setSelectedNode(editor.model.getNodeAt(e.getPoint()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePreviousPosition.setLocation(e.getX(), e.getY());
        if(selectionOn) editor.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePreviousPosition.setLocation(e.getX(), e.getY());
        if(selectionOn) editor.repaint();
    }

    public void mousePressed(MouseEvent e) {
        // if user presses down on an already selected item then user is dragging selected nodes
        Node n = editor.getGraph().getNodeAt(e.getPoint());
        if(n!=null) {
            if(!editor.getSelectedNodes().contains(n)) {
                editor.setSelectedNode(n);
            }
        } else {
            // nothing under point, start new selection.
            beginSelectionArea(e.getPoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(selectionOn) endSelectionArea(e.getPoint());
    }

    /**
     * What to do when the rectangle selection begins.
     * @param point the first corner of the rectangle selection area.
     */
    private void beginSelectionArea(Point point) {
        selectionOn=true;
        selectionAreaStart.x=point.x;
        selectionAreaStart.y=point.y;
    }

    /**
     * What to do when the rectangle selection ends.
     * @param point the second corner of the rectangle selection area.
     */
    private void endSelectionArea(Point point) {
        selectionOn=false;
        java.util.List<Node> nodesInSelectionArea = editor.getGraph().getNodesInRectangle(getSelectionArea(point));
        if(!keyStateMemory.isShiftKeyDown()) {
            editor.setSelectedNodes(nodesInSelectionArea);
        } else {
            java.util.List<Node> already = editor.getSelectedNodes();
            List<Node> overlap = new ArrayList<>(nodesInSelectionArea);
            overlap.retainAll(already);
            nodesInSelectionArea.removeAll(overlap);
            if(!overlap.isEmpty()) {
                already.removeAll(overlap);
            }
            already.addAll(nodesInSelectionArea);
        }
        editor.repaint();
    }

    /**
     * Returns the rectangle formed by the first selection point and this new point.
     * @param point the second point of the selection area.
     * @return the rectangle formed by the first selection point and this new point.
     */
    private Rectangle2D getSelectionArea(Point point) {
        double x1 = Math.min(point.x, selectionAreaStart.x);
        double x2 = Math.max(point.x, selectionAreaStart.x);
        double y1 = Math.min(point.y, selectionAreaStart.y);
        double y2 = Math.max(point.y, selectionAreaStart.y);
        return new Rectangle2D.Double(x1,y1,x2-x1,y2-y1);
    }
}
