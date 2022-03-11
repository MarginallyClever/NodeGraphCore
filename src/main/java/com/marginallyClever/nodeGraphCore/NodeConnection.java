package com.marginallyClever.nodeGraphCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.Objects;

/**
 * Describes the connection between two {@link NodeVariable}s in different {@link Node}s.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NodeConnection {
    /**
     * radius of connection points at either end.  Used for generating bounds and testing intersections.
     */
    public static final double DEFAULT_RADIUS = 5;

    private Node inNode;
    private int inVariableIndex=-1;
    private Node outNode;
    private int outVariableIndex=-1;

    /**
     * public Constructor for subclasses to call.
     */
    public NodeConnection() {
        super();
    }

    /**
     * Construct this {@link NodeConnection} with the given parameters.
     * @param inNode the input {@link Node}
     * @param inVariableIndex the {@link NodeVariable} index
     * @param outNode the output {@link Node}
     * @param outVariableIndex the output {@link NodeVariable} index
     */
    public NodeConnection(Node inNode,int inVariableIndex,Node outNode,int outVariableIndex) {
        this();
        setInput(inNode,inVariableIndex);
        setOutput(outNode,outVariableIndex);
    }

    /**
     * Construct this {@link NodeConnection} to match another.
     * @param another the source to match.
     */
    public NodeConnection(NodeConnection another) {
        this(another.inNode,another.inVariableIndex,another.outNode,another.outVariableIndex);
    }

    /**
     * Send the value of upstream variables to downstream variables if the upstream is dirty.
     */
    public void applyIfDirty() {
        if(!isValidDataType()) return;

        NodeVariable<?> in = getInputVariable();
        if(in.getIsDirty()) {
            getOutputVariable().setValue(in.getValue());
        }
    }

    /**
     * Send the value of upstream variables to downstream variables, dirty or not.
     */
    public void apply() {
        if(!isValidDataType()) return;

        NodeVariable<?> in = getInputVariable();
        getOutputVariable().setValue(in.getValue());
    }

    /**
     * @return true if the data type at both ends is a valid match.
     */
    public boolean isValidDataType() {
        if(!isInputValid() || !isOutputValid()) return false;
        NodeVariable<?> in = getInputVariable();
        NodeVariable<?> out = getOutputVariable();
        return out.isValidType(in.getValue());
    }

    /**
     * @return the {@link Node} connected on the input side.  May be null.
     */
    public Node getInNode() {
        return inNode;
    }

    /**
     * @return the {@link Node} connected on the output side.  May be null.
     */
    public Node getOutNode() {
        return outNode;
    }

    /**
     * @return the {@link NodeVariable} connected on the input side.
     * @throws IndexOutOfBoundsException if the requested index is invalid.
     * @throws NullPointerException if the output does not exist.
     */
    private NodeVariable<?> getOutputVariable() throws NullPointerException, IndexOutOfBoundsException {
        if(outNode==null) throw new NullPointerException("output does not exist");
        return outNode.getVariable(outVariableIndex);
    }

    private NodeVariable<?> getInputVariable() throws NullPointerException, IndexOutOfBoundsException {
        if(inNode==null) throw new NullPointerException("output does not exist");
        return inNode.getVariable(inVariableIndex);
    }

    /**
     * @return true if the input side of this connection is sane.
     */
    public boolean isInputValid() {
        if(inNode==null) return false;
        if(inVariableIndex==-1) return false;
        if(inNode.getNumVariables() <= inVariableIndex) return false;
        if(!inNode.getVariable(inVariableIndex).getHasOutput()) return false;
        return true;
    }

    /**
     * @return true if the output side of this connection is sane.
     */
    public boolean isOutputValid() {
        if(outNode==null) return false;
        if(outVariableIndex==-1) return false;
        if(outNode.getNumVariables() <= outVariableIndex) return false;
        if(!outNode.getVariable(outVariableIndex).getHasInput()) return false;
        return true;
    }

    /**
     * Sets the input of this {@link NodeConnection}.  Does not perform a validity check.
     * @param n the connecting {@link Node}
     * @param variableIndex the connecting node index.
     */
    public void setInput(Node n, int variableIndex) {
        inNode = n;
        inVariableIndex = variableIndex;
        apply();
    }

    /**
     * Sets the output of this {@link NodeConnection}.  Does not perform a validity check.
     * @param n the connecting {@link Node}
     * @param variableIndex the connecting node index.
     */
    public void setOutput(Node n, int variableIndex) {
        outNode = n;
        outVariableIndex = variableIndex;
        apply();
    }

    @Override
    public String toString() {
        return "NodeConnection{" +
                "inNode=" + (inNode==null ? "null" : inNode.getUniqueName()) +
                ", inVariableIndex=" + inVariableIndex +
                ", outNode=" + (outNode==null ? "null" : outNode.getUniqueName()) +
                ", outVariableIndex=" + outVariableIndex +
                '}';
    }

    /**
     * @return The position of this {@link NodeConnection}'s input connection point.
     */
    public Point getInPosition() {
        return inNode.getOutPosition(inVariableIndex);
    }

    /**
     * @return The position of this {@link NodeConnection}'s output connection point.
     */
    public Point getOutPosition() {
        return outNode.getInPosition(outVariableIndex);
    }

    /**
     * Returns true if this {@link NodeConnection} is attached at either end to a given {@link Node}.
     * @param node the subject being tested.
     * @return true if this {@link NodeConnection} is attached at either end to a given {@link Node}.
     */
    public boolean isConnectedTo(Node node) {
        return node==inNode || node==outNode;
    }

    /**
     * Disconnects from all {@link Node}s.
     */
    public void disconnectAll() {
        setInput(null,0);
        setOutput(null,0);
    }

    /**
     * Sets the contents of this {@link NodeConnection} to match that of another.
     * @param nodeConnection the {@link NodeConnection} to match.
     */
    public void set(NodeConnection nodeConnection) {
        setInput(nodeConnection.inNode,nodeConnection.inVariableIndex);
        setOutput(nodeConnection.outNode,nodeConnection.outVariableIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeConnection that = (NodeConnection) o;
        return inVariableIndex == that.inVariableIndex &&
                outVariableIndex == that.outVariableIndex &&
                inNode.equals(that.inNode) &&
                outNode.equals(that.outNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inNode, inVariableIndex, outNode, outVariableIndex);
    }

    /**
     * @return the index of the input {@link Node} variable to which this {@link NodeConnection} is attached.  Assumes
     * there is a valid connection.
     */
    public int getInVariableIndex() {
        return inVariableIndex;
    }

    /**
     * @return the index of the output {@link Node} variable to which this {@link NodeConnection} is attached.  Assumes
     * there is a valid connection.
     */
    public int getOutVariableIndex() {
        return outVariableIndex;
    }

    /**
     * @return the {@link NodeVariable} at this input, or null.
     */
    public NodeVariable<?> getInVariable() {
        return (inNode==null) ? null : inNode.getVariable(inVariableIndex);
    }

    /**
     * @return the {@link NodeVariable} at this output, or null.
     */
    public NodeVariable<?> getOutVariable() {
        return (outNode==null) ? null : outNode.getVariable(outVariableIndex);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        if(inNode!=null) {
            jo.put("inNode",inNode.getUniqueName());
            jo.put("inVariableIndex",inVariableIndex);
        }
        if(outNode!=null) {
            jo.put("outNode", outNode.getUniqueName());
            jo.put("outVariableIndex", outVariableIndex);
        }
        return jo;
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle();
        if(inNode!=null) {
            r.setLocation(getInPosition());
            if(outNode!=null) {
                r.add(getOutPosition());
            }
        } else if(outNode!=null) {
            r.setLocation(getOutPosition());
        }
        return r;
    }
}
