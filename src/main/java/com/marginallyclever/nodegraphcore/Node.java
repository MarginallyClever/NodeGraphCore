package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.port.Output;
import com.marginallyclever.nodegraphcore.port.Port;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.json.RectangleDAO4JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>{@link Node} is a collection of zero or more inputs and zero or more outputs connected by some operator.
 * The operator is defined by extending the {@link Node} class and defining the {@code update()} method.</p>
 * @author Dan Royer
 * @since 2022-02-01
 */
public abstract class Node {
    /**
     * The default height of the title bar.
     */
    public static final int TITLE_HEIGHT = 25;

    private String uniqueID = UUID.randomUUID().toString();

    private final String name;

    private String label;

    private final Rectangle rectangle = new Rectangle(0,0,150,50);

    private final List<Port<?>> ports = new ArrayList<>();

    /**
     * The percentage of completion of this node.
     */
    private final AtomicInteger complete = new AtomicInteger(0);

    /**
     * Default constructor
     * @param name the name of the class of this type of Node, for serialization and user selection.
     *             Developers of derived classes should not change this name after it is in production
     *             - it will break serialization.
     */
    protected Node(String name) {
        super();
        this.name = name;
        this.label = "";
    }

    /**
     * Sets the unique ID of this Node.
     */
    public void setUniqueID() {
        uniqueID=UUID.randomUUID().toString();
    }

    /**
     * Returns the unique ID of this Node.
     * @return the unique ID of this Node.
     */
    public String getUniqueID() {
        return uniqueID;
    }

    /**
     * @return the list of ports in this node.
     */
    public List<Port<?>> getPorts() {
        return ports;
    }

    /**
     * Sets the bounding rectangle for this node.
     * @param rectangle the new bounds.
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle.setBounds(rectangle);
    }

    /**
     * Returns the bounding rectangle for this node.
     * @return the bounding rectangle for this node.
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Returns the name of this node.
     * @return the name of this node.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the unique name of this node, a combination of unique ID and name.
     * @return the unique name of this node, a combination of unique ID and name.
     */
    public String getUniqueName() {
        return uniqueID+"-"+name;
    }

    /**
     * Override this method to provide the custom behavior of this node.  Runs regardless of dirty inputs or outputs.
     * Classes that derive from {@link Node} MUST not modify data that arrives from the inputs.
     * Classes that derive from {@link Node} MAY pass input along as output.
     */
    public abstract void update();

    /**
     * Recalculate the bounds of this node.
     */
    public void updateBounds() {
        int w=(int)rectangle.getWidth();
        int h=Node.TITLE_HEIGHT;
        var rect = getRectangle();
        int x=rect.x;
        int y=rect.y;
        for(Port<?> v : ports) {
            Rectangle r = v.getRectangle();
            r.y=h+y;
            r.x=x;
            if(w < r.width) w = r.width;
            h += r.height;
        }
        rectangle.width=w;
        rectangle.height=h;
    }

    /**
     * Add a {@link Port} to this node.
     * @param v the new {@link Port}
     */
    protected void addPort(Port<?> v) {
        ports.add(v);
    }

    /**
     * Remove a {@link Port} from this node.
     * @param v the old {@link Port}
     */
    protected void removePort(Port<?> v) {
        ports.remove(v);
    }

    /**
     * @return the number of ports in this node.
     */
    public int getNumPorts() {
        return ports.size();
    }

    /**
     * Get the i-th {@link Port} in this node.
     * @param index the index.
     * @return the i-th {@link Port} in this node.
     * @throws IndexOutOfBoundsException when an invalid index is requested.
     */
    public Port<?> getPort(int index) throws IndexOutOfBoundsException {
        return ports.get(index);
    }

    /**
     * @param name the name to match.
     * @return the {@link Port} found or null
     */
    public Port<?> getPort(String name) {
        for(Port<?> v : ports) {
            if(v.getName().equals(name)) return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name=" + getName() +
                ", uniqueID=" + getUniqueID() +
                ", label=" + label +
                ", rectangle=" + rectangle +
                ", ports=" + ports +
                '}';
    }

    /**
     * Returns the center of the input connection point of the requested {@link Port}.
     * @param index the requested index
     * @return the center of the input connection point of the requested {@link Port}.
     */
    public Point getInPosition(int index) {
        Rectangle r = getRectangle();
        return new Point(r.x,r.y+(int)getPointHeight(index));
    }

    /**
     * Returns the center of the output connection point of the requested {@link Port}.
     * @param index the requested index
     * @return the center of the output connection point of the requested {@link Port}.
     */
    public Point getOutPosition(int index) {
        Rectangle r = getRectangle();
        return new Point(r.x+r.width,r.y+(int)getPointHeight(index));
    }

    private double getPointHeight(int index) {
        double y = TITLE_HEIGHT;
        for(int i=0;i<index;++i) {
            y += getPort(i).getRectangle().height;
        }
        y += getPort(index).getRectangle().height/2.0;
        return y;
    }

    /**
     * Returns the label (nickname) of this node.
     * @return the label (nickname) of this node.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label for this node.
     * @param str the new label.
     */
    public void setLabel(String str) {
        label=str;
    }

    /**
     * Sets the top left corner of the {@link Node}'s rectangle.
     * @param point the new position of the top left corner.
     */
    public void setPosition(Point point) {
        rectangle.x=point.x;
        rectangle.y=point.y;
        updateBounds();
    }

    /**
     * Move this node some relative cartesian value.
     * @param dx the x axis amount.
     * @param dy the y axis amount.
     */
    public void moveRelative(int dx, int dy) {
        rectangle.x += dx;
        rectangle.y += dy;
        updateBounds();
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name",name);
        jo.put("uniqueID",uniqueID);
        jo.put("label", label);
        RectangleDAO4JSON dao = new RectangleDAO4JSON();
        jo.put("rectangle", dao.toJSON(rectangle));
        jo.put("variables", getAllPortsAsJSON());
        return jo;
    }

    private JSONArray getAllPortsAsJSON() {
        JSONArray vars = new JSONArray();
        for(Port<?> v : ports) {
            vars.put(v.toJSON());
        }
        return vars;
    }

    public void parseJSON(JSONObject jo) throws JSONException {
        String joName = jo.getString("name");
        if(!name.equals(joName)) throw new JSONException("Node types do not match: "+name+", "+joName);

        Object uid = jo.get("uniqueID");
        uniqueID = (uid instanceof String) ? (String)uid : uid.toString();

        if(jo.has("label")) {
            String s = jo.getString("label");
            if(!s.equals("null")) label = s;
        }
        RectangleDAO4JSON dao = new RectangleDAO4JSON();
        rectangle.setBounds(dao.fromJSON(jo.getJSONObject("rectangle")));
        parseAllPortsFromJSON(jo.getJSONArray("variables"));
    }

    private void parseAllPortsFromJSON(JSONArray vars) throws JSONException {
        //guaranteeSameNumberOfPorts(vars);
        for(int i=0;i<vars.length();++i) {
            var obj = vars.getJSONObject(i);
            var port = ports.get(i);
            // if a Node is changed later the JSON and the Node might not match.
            // if the port is null, it's a new port that wasn't in the JSON.
            // if the obj is null, it's a port that was in the JSON but isn't in the Node.
            if(obj != null && port != null) {
                // both exist, so parse the JSON into the port.
                port.fromJSON(obj);
            }
        }
    }

    private void guaranteeSameNumberOfPorts(JSONArray vars) throws JSONException {
        if(vars.length() != ports.size()) {
            int a = ports.size();
            int b = vars.length();
            throw new JSONException("JSON bad number of node ports.  Expected "+a+" found "+b);
        }
    }

    @Deprecated
    public int countReceivingConnections() {
        int count = 0;
        for(Port<?> v : ports) {
            if(v instanceof Input<?> k && k.hasConnection()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Classes which extend can use this method to reset their internal state as needed.  A good example is {@link
     * com.marginallyclever.nodegraphcore.nodes.LoadNumber} which only fires once when the program begins.
     */
    public void reset() {}

    /**
     * @return a list of all the nodes that are downstream from this node.
     */
    public List<Node> getDownstreamNodes() {
        List<Node> downstreamNodes = new ArrayList<>();
        for(var v : ports) {
            if(v instanceof Output<?> k) {
                for( var to : k.getTo()) {
                    downstreamNodes.add(to.getOtherNode(this));
                }
            }
        }
        return downstreamNodes;
    }

    /**
     * @return true if any of the Input<> Ports are dirty.
     */
    public boolean isDirty() {
        for(var v : ports) {
            if(v instanceof Input<?> k && k.isDirty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the percentage of completion of this node.
     */
    public int getComplete() {
        return complete.get();
    }

    /**
     * Sets the percentage of completion of this node.
     * @param percent the new percentage of completion.
     */
    public void setComplete(int percent) {
        this.complete.set(percent);
    }
}
