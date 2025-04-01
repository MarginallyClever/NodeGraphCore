package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.port.Port;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * {@link ConnectionPointInfo} makes it easier to query connection points of a {@link Port} within a
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

    private int portIndex;

    private int flags;

    /**
     * Constructor for subclasses to call.
     */
    protected ConnectionPointInfo() {}

    /**
     * Constructor for subclasses to call.
     * @param node a {@link Node}.
     * @param portIndex the index to a variable inside the node.
     * @param flags {@link ConnectionPointInfo#IN} or {@link ConnectionPointInfo#OUT}
     * @throws IllegalArgumentException if the given portIndex is less than 0.
     * @throws IllegalArgumentException if the given flags is not {@link ConnectionPointInfo#IN} or {@link ConnectionPointInfo#OUT}
     */
    public ConnectionPointInfo(@Nonnull Node node, int portIndex, int flags) {
        if(portIndex <0) throw new IllegalArgumentException("port index cannot be negative");
        if(flags!=IN && flags!=OUT) throw new IllegalArgumentException("flags must be IN or OUT");

        this.node=node;
        this.portIndex = portIndex;
        this.flags=flags;
    }

    /**
     * Returns the variable
     * @return the variable
     */
    public Port<?> getVariable() {
        return getNode().getPort(getPortIndex());
    }

    public Point getPoint() {
        Port<?> v = getVariable();
        return (getFlags() == IN) ? v.getInPosition() : v.getOutPosition();
    }

    /**
     * The index of the {@link Port} within this node with the connection point.
     */
    public int getPortIndex() {
        return portIndex;
    }

    /**
     * The {@link Node} containing the {@link Port} with a connection point.
     */
    public @Nonnull Node getNode() {
        return node;
    }

    /**
     * {@link ConnectionPointInfo#IN} or {@link ConnectionPointInfo#OUT}.
     */
    public int getFlags() {
        return flags;
    }
}
