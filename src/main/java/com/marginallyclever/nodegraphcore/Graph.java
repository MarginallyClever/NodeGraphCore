package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;
import com.marginallyclever.nodegraphcore.port.Port;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Graph} is a Node which contains {@link Node}s and {@link Connection}s.
 * It also maintains a list of {@link Port}s exposed to owner of this {@link Graph}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Graph extends Node {
    private static final Logger logger = LoggerFactory.getLogger(Graph.class);

    /**
     * The list of all {@link Node} in this graph.
     */
    private final List<Node> nodes = new ArrayList<>();

    /**
     * The list of all {@link Connection} in this graph.
     */
    private final List<Connection> connections = new ArrayList<>();

    /**
     * Constructor for subclasses to call.  Creates an empty {@link Graph}.
     */
    public Graph() {
        super("Graph");
    }

    /**
     * Queue the update of all dirty {@link Node}s in this {@link Graph}.
     */
    public void update() {
        // TODO somehow dirty nodes have to be submitted to the ThreadPoolScheduler here.

        for(Node n : nodes) {
            if(n.isDirty()) {
                n.update();
            }
        }
    }

    /**
     * @return a {@link List} of all the {@link Node}s within this {@link Graph}.
     * It is not a copy!  Use with caution.
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * @return a {@link List} of all the {@link Connection}s within this {@link Graph}.
     * It is not a copy!  Use with caution.
     */
    public List<Connection> getConnections() {
        return connections;
    }

    public boolean contains(Node node) {
        return nodes.contains(node);
    }

    /**
     * Adds a node to this graph.
     * @param node the subject
     * @return the same node for convenient method chaining.
     * @throws IllegalStateException if the node is already in the graph.
     */
    public Node add(Node node) {
        if(nodes.contains(node)) throw new IllegalStateException("Node added twice");
        nodes.add(node);
        return node;
    }

    /**
     * Remove a {@link Node} and all associated {@link Connection}s from the model.
     * @param n the subject to be removed.
     */
    public void remove(Node n) {
        nodes.remove(n);
        removeConnectionsToNode(n);
    }

    /**
     * Adds a {@link Connection} without checking if it already exists.
     * @param connection the item to add.
     * @return the same connection for convenient method chaining.
     */
    public Connection add(Connection connection) {
        connections.add(connection);
        return connection;
    }

    /**
     * Remove one {@link Connection} from this graph.
     * @param c the item to remove.
     */
    public void remove(Connection c) {
        connections.remove(c);
        c.disconnectAll();
    }

    /**
     * Add all {@link Node}s and {@link Connection}s from one model to this model.
     * @param graph the model to add.
     */
    public void add(Graph graph) {
        if(graph ==null) throw new IllegalArgumentException("nodeGraph cannot be null.");
        assignNewUniqueIDs();
        graph.assignNewUniqueIDs();

        nodes.addAll(graph.nodes);
        connections.addAll(graph.connections);
    }

    public void remove(Graph graph) {
        if(graph ==null) throw new IllegalArgumentException("nodeGraph cannot be null.");
        nodes.removeAll(graph.nodes);
        connections.removeAll(graph.connections);
    }


    /**
     * Remove all {@link Connection}s from the model associated with a given {@link Node}
     * @param n the subject from which all connections should be removed.
     */
    public void removeConnectionsToNode(Node n) {
        ArrayList<Connection> toRemove = new ArrayList<>();
        for(Connection c : connections) {
            for(int i = 0; i<n.getNumPorts(); ++i) {
                Port<?> v = n.getPort(i);
                if(c.getInput()==v || c.getOutput()==v) {
                    toRemove.add(c);
                }
            }
        }
        connections.removeAll(toRemove);
    }

    /**
     * Searches this {@link Graph} for an equivalent {@link Connection}.
     * @param connection the item to match.
     * @return returns the matching {@link Connection} or null.
     */
    public Connection getMatchingConnection(Connection connection) {
        for(Connection c : connections) {
            if(c.equals(connection)) return c;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                ", connections=" + connections +
                '}';
    }

    /**
     * Empty the model.
     */
    public void clear() {
        nodes.clear();
        connections.clear();
    }

    /**
     * Return the first connection point found within radius of a point
     * @param point center of search area
     * @param radius radius limit
     * @return a {@link ConnectionPointInfo} describing the point found or null.
     */
    public ConnectionPointInfo getNearestConnectionPoint(Point point, double radius) {
        double rr=radius*radius;
        ConnectionPointInfo info=null;

        for(Node n : nodes) {
            // early reject if the Node is not in the search area.
            Rectangle rect = n.getRectangle();
            if(point.x<rect.x-radius || point.x>rect.x+rect.width+radius) continue;
            if(point.y<rect.y-radius || point.y>rect.y+rect.height+radius) continue;

            // now check each Port in the Node.
            for(int i = 0; i < n.getNumPorts(); ++i) {
                Port<?> v = n.getPort(i);
                if(v instanceof Input) {
                    double r2 = v.getInPosition().distanceSq(point);
                    if (r2 < rr) {
                        rr = r2;
                        info = new ConnectionPointInfo(n, i, ConnectionPointInfo.IN);
                    }
                } else if(v instanceof Output) {
                    double r2 = v.getOutPosition().distanceSq(point);
                    if (r2 < rr) {
                        rr = r2;
                        info = new ConnectionPointInfo(n,i, ConnectionPointInfo.OUT);
                    }
                }
            }
        }

        return info;
    }

    /**
     * Returns the Node that matches the unique name, or null.
     * @param uniqueName the string to match.
     * @return the Node that matches the unique name, or null.
     */
    public Node findNodeWithUniqueName(String uniqueName) {
        for(Node n : nodes) {
            if(n.getUniqueName().equals(uniqueName)) return n;
        }
        return null;
    }

    /**
     * Find and remove any {@link Connection} that connects to the input side of a given {@link Port}.
     * @param input the {@link Port} with an input to be isolated.
     */
    public void removeAllConnectionsInto(Input<?> input) {
        connections.removeAll(getAllConnectionsInto(input));
    }

    /**
     * There should be only one Connection per Input but it is possible to build many Connections that all connect to
     * the same Input.  This method returns all Connections that claim to connect to the given Input.
     * @param input the {@link Input} to search for.
     * @return a {@link List} of all {@link Connection}s that connect to the given {@link Input}.
     */
    public List<Connection> getAllConnectionsInto(Input<?> input) {
        ArrayList<Connection> list = new ArrayList<>();

        for(Connection c : connections) {
            if(c.getInput() == input) list.add(c);
        }

        return list;
    }

    private void assignNewUniqueIDs() {
        for(Node n : nodes) n.setUniqueID();
    }

    /**
     * Returns a deep copy of this {@link Graph} by using the JSON serialization methods.
     * @return the {@link Graph} copy
     */
    public Graph deepCopy() {
        Graph copy = new Graph();
        copy.parseJSON(toJSON());
        return copy;
    }

    /**
     * Return the index within the list of nodes of the first occurrence of class c.
     * Comparisons are done using {@code isInstance()} and may return subclasses.
     * @param c the class to match.
     * @return the index within the list of nodes of the first occurrence of class c, or -1.
     */
    public int indexOfNode(Class<?> c) {
        return indexOfNode(c,0);
    }

    /**
     * Return the index within the list of nodes of the first occurrence of class c, starting the search at the
     * specified index.
     * Comparisons are done using {@code isInstance()} and may return subclasses.
     * @param c the class to match.
     * @param fromIndex the index to start the search from.
     * @return the index within the list of nodes of the first occurrence of class c, or -1.
     */
    public int indexOfNode(Class<?> c, int fromIndex) {
        for(int i=fromIndex;i<nodes.size();++i) {
            if(c.isInstance(nodes.get(i))) return i;
        }
        return -1;
    }

    /**
     * Return the number of instances of class c or its subclasses.
     * Comparisons are done using {@code isInstance()} and may count subclasses.
     * @param c the class to match
     * @return the number of instances of class c or its subclasses.
     */
    public int countNodesOfClass(Class<?> c) {
        int i=0;
        for(Node n : nodes) {
            if(c.isInstance(n)) i++;
        }
        return i;
    }

    /**
     * Returns a {@link List} of all {@link Node}s that intersect the given rectangle
     * @param searchArea the search area
     * @return a {@link List} of all {@link Node}s that intersect the given rectangle
     */
    public List<Node> getNodesInRectangle(Rectangle searchArea) {
        if(searchArea==null) throw new InvalidParameterException("selectionArea cannot be null.");
        ArrayList<Node> found = new ArrayList<>();
        for(Node n : nodes) {
            if(searchArea.intersects(n.getRectangle())) found.add(n);
        }

        return found;
    }

    /**
     * Returns true if the node list is empty.
     * @return true if the node list is empty.
     */
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    /**
     * Calculates and returns the smallest {@link Rectangle} that contains all {@link Node}s.  If there are no Nodes
     * in this graph then nothing is done.
     * @return the smallest {@link Rectangle} that contains all {@link Node}s, or null.
     */
    public Rectangle getBounds() {
        if(nodes.isEmpty()) return null;

        Rectangle r=new Rectangle(nodes.get(0).getRectangle());
        for(Node n : nodes) {
            r.add(n.getRectangle());
            // for very small graphs this is a redundant union with self.
            // For very large graphs this avoids any 'if' in the loop and saves time.
        }
        return r;
    }

    /**
     * update bounds of all nodes in this graph.
     */
    public void updateBounds() {
        for(Node n : nodes) {
            n.updateBounds();
        }
    }

    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        jo.put("nodes",getAllNodesAsJSON());
        jo.put("connections",getAllNodeConnectionsAsJSON());
        return jo;
    }

    public void parseJSON(JSONObject jo) throws JSONException {
        clear();
        parseAllNodesFromJSON(jo.getJSONArray("nodes"));
        parseAllConnectionsFromJSON(jo.getJSONArray("connections"));
    }

    private void parseAllNodesFromJSON(JSONArray arr) throws JSONException {
        for (Object element : arr) {
            JSONObject o = (JSONObject)element;
            Node n = NodeFactory.createNode(o.getString("name"));
            n.parseJSON(o);
            add(n);
        }
    }

    private void parseAllConnectionsFromJSON(JSONArray arr) throws JSONException {
        for (Object o : arr) {
            try {
                Connection c = new Connection();
                parseOneConnectionFromJSON(c, (JSONObject) o);
                add(c);
            } catch(Exception e) {
                logger.error("Error parsing connection",e);
            }
        }
    }

    /**
     * {@link Connection} must be parsed in the {@link Graph} because only here can we access the list of
     * nodes to find the one with a matching {@code getUniqueName()}.
     * @param c the connection to parse into.
     * @param jo the JSON to parse.
     */
    private void parseOneConnectionFromJSON(Connection c, JSONObject jo) {
        if(jo.has("from")) {
            Node n = findNodeWithUniqueName(jo.getString("from"));
            int i = jo.getInt("fromIndex");
            c.setFrom(n,i);
        }
        if(jo.has("to")) {
            Node n = findNodeWithUniqueName(jo.getString("to"));
            int i = jo.getInt("toIndex");
            c.setTo(n,i);
        }
        if(jo.has("inNode")) {
            Node n = findNodeWithUniqueName(jo.getString("inNode"));
            int i = jo.getInt("inVariableIndex");
            c.setFrom(n,i);
        }
        if(jo.has("outNode")) {
            Node n = findNodeWithUniqueName(jo.getString("outNode"));
            int i = jo.getInt("outVariableIndex");
            c.setTo(n,i);
        }
    }

    private JSONArray getAllNodesAsJSON() {
        JSONArray a = new JSONArray();
        for (Node n : nodes) {
            a.put(n.toJSON());
        }
        return a;
    }

    private JSONArray getAllNodeConnectionsAsJSON() {
        JSONArray a = new JSONArray();
        for (Connection c : connections) {
            a.put(c.toJSON());
        }
        return a;
    }

    /**
     * Return the last {@link Node} at the given point, which will be the top-most visible.
     * @param point the search location.
     * @return the last {@link Node} at the given point
     */
    public Node getNodeAt(Point point) {
        List<Node> list = getNodes();
        // reverse iterator because last node is top-most.
        for (int i = list.size(); i-- > 0; ) {
            Node n = list.get(i);
            if(n.getRectangle().contains(point)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Returns a list of connections that connect to the selected {@link Node}s and unselected {@link Node}s.
     * @param selectedNodes the set of {@link Node}s.
     * @return a list of connections that connect to the selected {@link Node}s and unselected {@link Node}s.
     */
    public List<Connection> getExteriorConnections(List<Node> selectedNodes) {
        return getConnectionsCounted(selectedNodes,1);
    }

    /**
     * Returns a list of connections that connect between the selected {@link Node}s.
     * @param selectedNodes the set of {@link Node}s.
     * @return a list of connections that connect between the selected {@link Node}s.
     */
    public List<Connection> getInteriorConnections(List<Node> selectedNodes) {
        return getConnectionsCounted(selectedNodes,2);
    }

    /**
     * Returns a list of connections that have exactly <code>count</code> connections to the selected {@link Node}s.
     * @param selectedNodes the set of {@link Node}s.
     * @param count the number of connections to match.
     * @return a list of connections that have exactly <code>count</code> connections to the selected {@link Node}s.
     */
    private List<Connection> getConnectionsCounted(List<Node> selectedNodes, int count) {
        ArrayList<Connection> found = new ArrayList<>();

        for(Connection c : getConnections()) {
            int hits=0;
            for(Node n : selectedNodes) {
                if(c.isConnectedTo(n)) {
                    hits++;
                    if(hits==2) break;
                }
            }
            if(hits==count) {
                found.add(c);
            }
        }
        return found;
    }

    public void reset() {
        for(Node node : getNodes()) {
            node.reset();
        }
    }

    /**
     * @param port the port to check
     * @return true if the {@link Port} is attached to a {@link Connection}.
     */
    public boolean isPortConnected(Port<?> port) {
        for(Connection c : getConnections()) {
            if(c.getInput()==port || c.getOutput()==port) return true;
        }
        return false;
    }
}
