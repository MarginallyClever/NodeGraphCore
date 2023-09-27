package com.marginallyclever.nodegraphcore;

import java.awt.*;

/**
 * {@link ConnectionPointInfo} makes it easier to query connection points of a {@link Dock} within a
 * {@link Node}.  A use case is for remembering a new {@link Connection}'s starting point as it is being created.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class ConnectionPointInfo {
    /**
     * Indicates this connection point is an output from a {@link Node}.
     */
    public static final int OUT=2;

    /**
     * Indicates this connection point is an input to a {@link Node}.
     */
    public static final int IN=1;

    private Node node;

    private int dockIndex;

    private int flags;

    /**
     * Constructor for subclasses to call.
     */
    protected ConnectionPointInfo() {}

    /**
     * Constructor for subclasses to call.
     * @param node a {@link Node}.
     * @param dockIndex the index to a variable inside the node.
     * @param flags {@link ConnectionPointInfo#IN} or {@link ConnectionPointInfo#OUT}
     * @throws IllegalArgumentException if the given node is null.
     * @throws IllegalArgumentException if the given dockIndex is less than 0.
     * @throws IllegalArgumentException if the given flags is not {@link ConnectionPointInfo#IN} or {@link ConnectionPointInfo#OUT}
     */
    public ConnectionPointInfo(Node node, int dockIndex, int flags) {
        if(node==null) throw new IllegalArgumentException("node cannot be null");
        if(dockIndex<0) throw new IllegalArgumentException("dockIndex cannot be negative");
        if(flags!=IN && flags!=OUT) throw new IllegalArgumentException("flags must be IN or OUT");

        this.node=node;
        this.dockIndex = dockIndex;
        this.flags=flags;
    }

    /**
     * Returns the variable
     * @return the variable
     */
    public Dock<?> getVariable() {
        return getNode().getVariable(getDockIndex());
    }

    public Point getPoint() {
        Dock<?> v = getVariable();
        return (getFlags() == IN) ? v.getInPosition() : v.getOutPosition();
    }

    /**
     * The index of the {@link Dock} within this node with the connection point.
     */
    public int getDockIndex() {
        return dockIndex;
    }

    /**
     * The {@link Node} containing the {@link Dock} with a connection point.
     */
    public Node getNode() {
        return node;
    }

    /**
     * {@link ConnectionPointInfo#IN} or {@link ConnectionPointInfo#OUT}.
     */
    public int getFlags() {
        return flags;
    }
}
