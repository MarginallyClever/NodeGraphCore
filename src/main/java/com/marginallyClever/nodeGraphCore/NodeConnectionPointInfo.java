package com.marginallyClever.nodeGraphCore;

/**
 * {@link NodeConnectionPointInfo} makes it easier to query connection points of a {@link NodeVariable} within a
 * {@link Node}.  A use case is for remembering a new {@link NodeConnection}'s starting point as it is being created.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class NodeConnectionPointInfo {
    /**
     * The {@link Node} containing the {@link NodeVariable} with a connection point.
     */
    public Node node;

    /**
     * The index of the {@link NodeVariable} within this node with the connection point.
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

    public NodeConnectionPointInfo() {}

    public NodeConnectionPointInfo(Node node,int nodeVariableIndex,int flags) {
        this.node=node;
        this.nodeVariableIndex=nodeVariableIndex;
        this.flags=flags;
    }

    public NodeVariable<?> getVariable() {
        return node.getVariable(nodeVariableIndex);
    }
}
