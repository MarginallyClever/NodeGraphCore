package com.marginallyclever.nodegraphcore;

import java.awt.*;

/**
 * {@link NodeConnectionPointInfo} makes it easier to query connection points of a {@link Dock} within a
 * {@link Node}.  A use case is for remembering a new {@link Connection}'s starting point as it is being created.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class NodeConnectionPointInfo {
    /**
     * The {@link Node} containing the {@link Dock} with a connection point.
     */
    public Node node;

    /**
     * The index of the {@link Dock} within this node with the connection point.
     */
    public int nodeVariableIndex;

    /**
     * Indicates this connection point is an output from a {@link Node}.
     */
    public static final int OUT=2;

    /**
     * Indicates this connection point is an input to a {@link Node}.
     */
    public static final int IN=1;

    /**
     * {@link NodeConnectionPointInfo#IN} or {@link NodeConnectionPointInfo#OUT}.
     */
    public int flags;

    /**
     * Constructor for subclasses to call.
     */
    public NodeConnectionPointInfo() {}

    /**
     * Constructor for subclasses to call.
     * @param node a {@link Node}
     * @param nodeVariableIndex the index to a variable inside the node.
     * @param flags {@link NodeConnectionPointInfo#IN} or {@link NodeConnectionPointInfo#OUT}
     */
    public NodeConnectionPointInfo(Node node,int nodeVariableIndex,int flags) {
        this.node=node;
        this.nodeVariableIndex=nodeVariableIndex;
        this.flags=flags;
    }

    /**
     * Returns the variable
     * @return the variable
     */
    public Dock<?> getVariable() {
        return node.getVariable(nodeVariableIndex);
    }

    public Point getPoint() {
        Dock<?> v = getVariable();
        return (flags == IN) ? v.getInPosition() : v.getOutPosition();
    }
}
