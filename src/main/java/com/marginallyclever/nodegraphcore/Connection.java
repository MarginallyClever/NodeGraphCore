package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.port.Port;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.Objects;

/**
 * Describes the connection between two {@link Port}s in different {@link Node}s.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Connection {
    /**
     * radius of connection points at either end.  Used for generating bounds and testing intersections.
     */
    public static final double DEFAULT_RADIUS = 5;

    private Node from;
    private int fromIndex = -1;
    private Node to;
    private int toIndex = -1;

    /**
     * public Constructor for subclasses to call.
     */
    public Connection() {
        super();
    }

    /**
     * Construct this {@link Connection} with the given parameters.
     * @param from the input {@link Node}
     * @param fromIndex the {@link Port} index
     * @param to the output {@link Node}
     * @param toIndex the output {@link Port} index
     */
    public Connection(Node from, int fromIndex, Node to, int toIndex) {
        this();
        setFrom(from,fromIndex);
        setTo(to,toIndex);
    }

    /**
     * Construct this {@link Connection} to match another.
     * @param another the source to match.
     */
    public Connection(Connection another) {
        this();
        setFrom(another.from,another.fromIndex);
        setTo(another.to,another.toIndex);
    }

    /**
     * @return true if the data type at both ends is a valid match.
     */
    public boolean isValidDataType() {
        if (!isFromASaneOutput() || !isToASaneInput()) return false;
        Output<?> in = getOutput();
        Input<?> out = getInput();
        return out.isValidType(in.getValue());
    }

    /**
     * @return the {@link Input} connected on the input side.
     * @throws NullPointerException if the output does not exist.
     */
    public Input<?> getInput() throws NullPointerException, IndexOutOfBoundsException {
        if(to == null) return null;
        return (Input<?>)to.getPort(toIndex);
    }

    /**
     * @return the {@link Output} connected on the output side.
     * @throws NullPointerException if the output does not exist.
     */
    public Output<?> getOutput() throws NullPointerException, IndexOutOfBoundsException {
        if(from == null) return null;
        return (Output<?>)from.getPort(fromIndex);
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    /**
     * @return true if the from side of this connection is sane.
     */
    public boolean isFromASaneOutput() {
        if(from == null) return false;
        if(fromIndex < 0) return false;
        if(fromIndex >= from.getNumPorts()) return false;
        return from.getPort(fromIndex) instanceof Output<?>;
    }

    /**
     * @return true if the output side of this connection is sane.
     */
    public boolean isToASaneInput() {
        if(to == null) return false;
        if(toIndex < 0) return false;
        if(toIndex >= to.getNumPorts()) return false;
        return to.getPort(toIndex) instanceof Input<?>;
    }

    /**
     * Sets the input of this {@link Connection}.  Does not perform a validity check.
     * @param n the connecting {@link Node}
     */
    public void setFrom(Node n,int index) {
        from = n;
        fromIndex = index;
        if(n!=null) {
            ((Output<?>)n.getPort(index)).addTo(this);
        }
    }

    /**
     * Sets the output of this {@link Connection}.  Does not perform a validity check.
     * @param n the connecting {@link Node}
     */
    public void setTo(Node n,int index) {
        to = n;
        toIndex = index;
        if(n!=null) {
            ((Input<?>)n.getPort(index)).setFrom(this);
        }
    }

    @Override
    public String toString() {
        return "Connection{" +
                "from=" + (from==null ? "null" : from.getUniqueName()) +
                ", fromIndex=" + fromIndex +
                ", to=" + (to==null ? "null" : to.getUniqueName()) +
                ", toIndex=" + toIndex +
                '}';
    }

    /**
     * @return The position of this {@link Connection}'s input connection point.
     */
    public Point getInPosition() {
        return from.getOutPosition(fromIndex);
    }

    /**
     * @return The position of this {@link Connection}'s output connection point.
     */
    public Point getOutPosition() {
        return to.getInPosition(toIndex);
    }

    /**
     * @param n the subject being tested.
     * @return true if this {@link Connection} is attached at either end to a given {@link Node}.
     */
    public boolean isConnectedTo(Node n) {
        return n == from || n == to;
    }

    /**
     * Disconnects from all {@link Node}s.
     */
    public void disconnectAll() {
        if(getOutput()!=null) getOutput().removeTo(this);
        if(getInput()!=null) getInput().setFrom(null);
        // this connection may have been stored in an undo stack.
        // do not modify it so that it can be reconnected later.
    }

    /**
     * Sets the contents of this {@link Connection} to match that of another.
     * @param connection the {@link Connection} to match.
     */
    public void set(Connection connection) {
        setFrom(connection.from,connection.fromIndex);
        setTo(connection.to,connection.toIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if(!(other instanceof Connection c2)) return false;
        return Objects.equals(from, c2.from) &&
                Objects.equals(to, c2.to) &&
                fromIndex == c2.fromIndex &&
                toIndex == c2.toIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        if(from != null) {
            jo.put("from", from.getUniqueName());
            jo.put("fromIndex", fromIndex);
        }
        if(to != null) {
            jo.put("to", to.getUniqueName());
            jo.put("toIndex", toIndex);
        }
        return jo;
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle();
        if(from !=null) r.add(getInPosition());
        if(to !=null) r.add(getOutPosition());
        return r;
    }

    /**
     * @param node the node to check
     * @return the node from the other end of the connection
     */
    public Node getOtherNode(Node node) {
        if(node == from) return to;
        if(node == to) return from;
        return null;
    }

    public void apply() {
        // if the input and output are valid, set the input value to the output value.
        getInput().setValue(getOutput().getValue());
    }

    public void reset() {
        // if the input and output are valid, set the input value to the output value.
        getInput().setValueToDefault();
    }
}
