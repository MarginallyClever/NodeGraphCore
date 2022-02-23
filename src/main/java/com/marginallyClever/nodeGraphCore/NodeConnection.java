package com.marginallyClever.nodeGraphCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.Objects;

public class NodeConnection {
    public static final double DEFAULT_RADIUS = 5;

    private Node inNode;
    private int inVariableIndex=-1;
    private Node outNode;
    private int outVariableIndex=-1;

    public NodeConnection() {
        super();
    }

    public NodeConnection(Node inNode,int inVariableIndex,Node outNode,int outVariableIndex) {
        this();
        setInput(inNode,inVariableIndex);
        setOutput(outNode,outVariableIndex);
    }

    public NodeConnection(NodeConnection b) {
        this();
        set(b);
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

    public boolean isValidDataType() {
        if(!isInputValid() || !isOutputValid()) return false;
        NodeVariable<?> in = getInputVariable();
        NodeVariable<?> out = getOutputVariable();
        return out.isValidType(in.getValue());
    }

    public Node getInNode() {
        return inNode;
    }

    public Node getOutNode() {
        return outNode;
    }

    private NodeVariable<?> getOutputVariable() {
        return outNode.getVariable(outVariableIndex);
    }

    private NodeVariable<?> getInputVariable() {
        return inNode.getVariable(inVariableIndex);
    }

    public boolean isInputValid() {
        if(inNode==null) return false;
        if(inVariableIndex==-1) return false;
        if(inNode.getNumVariables() <= inVariableIndex) return false;
        if(!inNode.getVariable(inVariableIndex).getHasOutput()) return false;
        return true;
    }

    public boolean isOutputValid() {
        if(outNode==null) return false;
        if(outVariableIndex==-1) return false;
        if(outNode.getNumVariables() <= outVariableIndex) return false;
        if(!outNode.getVariable(outVariableIndex).getHasInput()) return false;
        return true;
    }

    public void setInput(Node n, int variableIndex) {
        inNode = n;
        inVariableIndex = variableIndex;
        apply();
    }

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

    // the in position of this {@link NodeConnection} is the out position of a {@link NodeVariable}
    public Point getInPosition() {
        return inNode.getOutPosition(inVariableIndex);
    }

    /**
     * The out position of this {@link NodeConnection} is the in position of a {@link NodeVariable}
     */
    public Point getOutPosition() {
        return outNode.getInPosition(outVariableIndex);
    }

    public boolean isConnectedTo(Node n) {
        return inNode==n || outNode==n;
    }

    public void disconnectAll() {
        setInput(null,0);
        setOutput(null,0);
    }

    public void set(NodeConnection b) {
        setInput(b.inNode,b.inVariableIndex);
        setOutput(b.outNode,b.outVariableIndex);
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

    public NodeVariable<?> getInVariable() {
        return (inNode==null) ? null : inNode.getVariable(inVariableIndex);
    }

    public NodeVariable<?> getOutVariable() {
        return (outNode==null) ? null : outNode.getVariable(outVariableIndex);
    }
}
