package com.marginallyClever.nodeGraphSwing.modalTools;

import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphCore.NodeConnectionPointInfo;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphCore.NodeVariable;
import com.marginallyClever.nodeGraphSwing.ModalTool;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;
import com.marginallyClever.nodeGraphSwing.NodeGraphViewPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConnectionEditTool extends ModalTool {
    private static final Color CONNECTION_POINT_COLOR_SELECTED = Color.RED;
    private static final Color CONNECTION_BEING_EDITED = Color.RED;
    private static final double NEARBY_CONNECTION_DISTANCE_MAX = 20;

    private final NodeGraphEditorPanel editor;

    /**
     * for tracking relative motion, useful for relative moves like dragging.
     */
    private final Point mousePreviousPosition = new Point();

    /**
     * To create a {@link NodeConnection} the user has to select two {@link NodeVariable} connection points.
     * This is where the first is stored until the user completes the connection or cancels the action.
     */
    private final NodeConnection connectionBeingCreated = new NodeConnection();

    /**
     * The last connection point found
     */
    private NodeConnectionPointInfo lastConnectionPoint = null;


    public ConnectionEditTool(NodeGraphEditorPanel editor) {
        this.editor=editor;
    }

    @Override
    public String getName() {
        return "Connect";
    }

    @Override
    public void paint(Graphics g) {
        paintConnectionBeingMade(g);
        highlightNearbyConnectionPoint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        onClickConnectionPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePreviousPosition.setLocation(e.getX(), e.getY());
        selectOneNearbyConnectionPoint(e.getPoint());
    }

    /**
     * Searches for a nearby {@link NodeVariable} connection point and, if found, remembers it.
     * @param p the center of the search area.
     */
    private void selectOneNearbyConnectionPoint(Point p) {
        NodeConnectionPointInfo info = editor.getGraph().getFirstNearbyConnection(p,NEARBY_CONNECTION_DISTANCE_MAX);
        setLastConnectionPoint(info);
    }

    /**
     * Remembers a connection point as described by a {@link NodeConnectionPointInfo}.
     * @param info the {@link NodeConnectionPointInfo}
     */
    private void setLastConnectionPoint(NodeConnectionPointInfo info) {
        lastConnectionPoint = info;
        editor.repaint();
    }

    /**
     * Paints a connection as it is being made
     * @param g the {@link Graphics} context
     */
    private void paintConnectionBeingMade(Graphics g) {
        if(connectionBeingCreated.isInputValid() || connectionBeingCreated.isOutputValid()) {
            g.setColor(CONNECTION_BEING_EDITED);
            NodeGraphViewPanel paintArea = editor.getPaintArea();
            paintArea.setLineWidth(g,3);

            Point a,b;
            if(connectionBeingCreated.isInputValid()) {
                a = connectionBeingCreated.getInPosition();
                b = mousePreviousPosition;
                paintArea.paintConnectionAtPoint(g,a);
            } else {
                a = mousePreviousPosition;
                b = connectionBeingCreated.getOutPosition();
                paintArea.paintConnectionAtPoint(g,b);
            }
            paintArea.paintBezierBetweenTwoPoints(g,a,b);

            paintArea.setLineWidth(g,1);
        }
    }

    /**
     * Paints the connection point under the cursor
     * @param g the {@link Graphics} context
     */
    private void highlightNearbyConnectionPoint(Graphics g) {
        if(lastConnectionPoint !=null) {
            g.setColor(CONNECTION_POINT_COLOR_SELECTED);
            NodeGraphViewPanel paintArea = editor.getPaintArea();
            paintArea.setLineWidth(g,2);
            paintArea.paintVariableConnectionPoints(g,lastConnectionPoint.getVariable());
            paintArea.setLineWidth(g,1);
        }
    }

    /**
     * What to do when a user clicks on a connection point.
     */
    private void onClickConnectionPoint() {
        if(lastConnectionPoint == null) {
            connectionBeingCreated.disconnectAll();
            return;
        }

        // check that the end node is not the same as the start node.
        if(!connectionBeingCreated.isConnectedTo(lastConnectionPoint.node)) {
            if (lastConnectionPoint.flags == NodeConnectionPointInfo.IN) {
                // the output of a connection goes to the input of a node.
                connectionBeingCreated.setOutput(lastConnectionPoint.node, lastConnectionPoint.nodeVariableIndex);
            } else {
                //the output of a node goes to the input of a connection.
                connectionBeingCreated.setInput(lastConnectionPoint.node, lastConnectionPoint.nodeVariableIndex);
            }
            editor.repaint();
        }

        if(connectionBeingCreated.isInputValid() && connectionBeingCreated.isOutputValid() ) {
            if(connectionBeingCreated.isValidDataType()) {
                NodeGraph graph = editor.getGraph();
                NodeConnection match = graph.getMatchingConnection(connectionBeingCreated);
                if(match!=null) graph.remove(match);
                else {
                    graph.removeAllConnectionsInto(connectionBeingCreated.getOutVariable());
                    graph.add(new NodeConnection(connectionBeingCreated));
                }
            } else {
                NodeVariable<?> vIn = connectionBeingCreated.getInVariable();
                NodeVariable<?> vOut = connectionBeingCreated.getOutVariable();
                String nameIn = (vIn==null) ? "null" : vIn.getTypeName();
                String nameOut = (vOut==null) ? "null" : vOut.getTypeName();
                System.out.println("Invalid types "+nameOut+", "+nameIn+".");
            }
            // if any of the tests failed, restart.
            connectionBeingCreated.disconnectAll();
            editor.repaint();
        }
    }

    public NodeConnectionPointInfo getLastConnectionPoint() {
        return lastConnectionPoint;
    }

    public void restart() {
        connectionBeingCreated.disconnectAll();
    }

    @Override
    public void attachKeyboardAdapter() {
        super.attachKeyboardAdapter();
    }

    @Override
    public void attachMouseAdapter() {
        super.attachMouseAdapter();
        NodeGraphViewPanel paintArea = editor.getPaintArea();
        paintArea.addMouseMotionListener(this);
        paintArea.addMouseListener(this);
    }
}
