package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.dock.Dock;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Describes the connection between two {@link Dock}s in different {@link Node}s.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Connection {
    /**
     * radius of connection points at either end.  Used for generating bounds and testing intersections.
     */
    public static final double DEFAULT_RADIUS = 5;

    private final Deque<Packet<?>> queue = new LinkedBlockingDeque<>();

    private Node inNode;
    private int inVariableIndex=-1;
    private Node outNode;
    private int outVariableIndex=-1;

    /**
     * public Constructor for subclasses to call.
     */
    public Connection() {
        super();
    }

    /**
     * Construct this {@link Connection} with the given parameters.
     * @param inNode the input {@link Node}
     * @param inVariableIndex the {@link Dock} index
     * @param outNode the output {@link Node}
     * @param outVariableIndex the output {@link Dock} index
     */
    public Connection(Node inNode, int inVariableIndex, Node outNode, int outVariableIndex) {
        this();
        setInput(inNode,inVariableIndex);
        setOutput(outNode,outVariableIndex);

        ((Output<?>)inNode.getVariable(inVariableIndex)).addTo(this);
        ((Input<?>)outNode.getVariable(outVariableIndex)).setFrom(this);
    }

    /**
     * Construct this {@link Connection} to match another.
     * @param another the source to match.
     */
    public Connection(Connection another) {
        this(another.inNode,another.inVariableIndex,another.outNode,another.outVariableIndex);
    }

    /**
     * @return true if the data type at both ends is a valid match.
     */
    public boolean isValidDataType() {
        if (!isInputValid() || !isOutputValid()) return false;
        Dock<?> in = getInputVariable();
        Dock<?> out = getOutputVariable();
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
     * @return the {@link Dock} connected on the input side.
     * @throws IndexOutOfBoundsException if the requested index is invalid.
     * @throws NullPointerException if the output does not exist.
     */
    private Dock<?> getOutputVariable() throws NullPointerException, IndexOutOfBoundsException {
        if(outNode==null) throw new NullPointerException("output does not exist");
        return outNode.getVariable(outVariableIndex);
    }

    private Dock<?> getInputVariable() throws NullPointerException, IndexOutOfBoundsException {
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
        if(!(inNode.getVariable(inVariableIndex) instanceof Output)) return false;
        return true;
    }

    /**
     * @return true if the output side of this connection is sane.
     */
    public boolean isOutputValid() {
        if(outNode==null) return false;
        if(outVariableIndex==-1) return false;
        if(outNode.getNumVariables() <= outVariableIndex) return false;
        if(!(outNode.getVariable(outVariableIndex) instanceof Input)) return false;
        return true;
    }

    /**
     * Sets the input of this {@link Connection}.  Does not perform a validity check.
     * @param n the connecting {@link Node}
     * @param variableIndex the connecting node index.
     */
    public void setInput(Node n, int variableIndex) {
        inNode = n;
        inVariableIndex = variableIndex;
        if(n!=null) {
            ((Output<?>) n.getVariable(variableIndex)).addTo(this);
        }
    }

    /**
     * Sets the output of this {@link Connection}.  Does not perform a validity check.
     * @param n the connecting {@link Node}
     * @param variableIndex the connecting node index.
     */
    public void setOutput(Node n, int variableIndex) {
        outNode = n;
        outVariableIndex = variableIndex;
        if(n!=null) {
            ((Input<?>) n.getVariable(variableIndex)).setFrom(this);
        }
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
     * @return The position of this {@link Connection}'s input connection point.
     */
    public Point getInPosition() {
        return inNode.getOutPosition(inVariableIndex);
    }

    /**
     * @return The position of this {@link Connection}'s output connection point.
     */
    public Point getOutPosition() {
        return outNode.getInPosition(outVariableIndex);
    }

    /**
     * Returns true if this {@link Connection} is attached at either end to a given {@link Node}.
     * @param node the subject being tested.
     * @return true if this {@link Connection} is attached at either end to a given {@link Node}.
     */
    public boolean isConnectedTo(Node node) {
        return node==inNode || node==outNode;
    }

    /**
     * Disconnects from all {@link Node}s.
     */
    public void disconnectAll() {
        if(getInVariable()!=null) ((Output<?>)getInVariable()).removeTo(this);
        if(getOutVariable()!=null) ((Input<?>)getOutVariable()).removeFrom(this);
        setInput(null,0);
        setOutput(null,0);
        queue.clear();
    }

    /**
     * Sets the contents of this {@link Connection} to match that of another.
     * @param connection the {@link Connection} to match.
     */
    public void set(Connection connection) {
        setInput(connection.inNode, connection.inVariableIndex);
        setOutput(connection.outNode, connection.outVariableIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
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
     * @return the index of the input {@link Node} variable to which this {@link Connection} is attached.  Assumes
     * there is a valid connection.
     */
    public int getInVariableIndex() {
        return inVariableIndex;
    }

    /**
     * @return the index of the output {@link Node} variable to which this {@link Connection} is attached.  Assumes
     * there is a valid connection.
     */
    public int getOutVariableIndex() {
        return outVariableIndex;
    }

    /**
     * @return the {@link Dock} at this input, or null.
     */
    public Dock<?> getInVariable() {
        return (inNode==null) ? null : inNode.getVariable(inVariableIndex);
    }

    /**
     * @return the {@link Dock} at this output, or null.
     */
    public Dock<?> getOutVariable() {
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

    /**
     * Returns the node from the other end of the connection
     * @param node the node to check
     * @return the node from the other end of the connection
     */
    public Node getOtherNode(Node node) {
        if(node==inNode) return outNode;
        else if(node==outNode) return inNode;
        else throw new GraphException("NodeConnection does not connect to given node.");
    }

    public void send(Packet<?> packet) {
        queue.offer(packet);
    }

    public Packet<?> get() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }
}
