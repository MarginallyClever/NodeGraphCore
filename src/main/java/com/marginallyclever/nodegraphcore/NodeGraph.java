package com.marginallyclever.nodegraphcore;

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
 * {@link NodeGraph} contains the {@link Node}s, and {@link NodeConnection}s
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NodeGraph {
    private static final Logger logger = LoggerFactory.getLogger(NodeGraph.class);

    /**
     * The list of all {@link Node} in this graph.
     */
    private final List<Node> nodes = new ArrayList<>();

    /**
     * The list of all {@link NodeConnection} in this graph.
     */
    private final List<NodeConnection> connections = new ArrayList<>();

    /**
     * Constructor for subclasses to call.  Creates an empty {@link NodeGraph}.
     */
    public NodeGraph() {
        super();
    }

    /**
     * <ul>
     * <li>Updates only dirty nodes.</li>
     * <li>Transmits dirty node outputs to connected inputs.</li>
     * <li>Sets all outputs to clean.</li>
     * </ul>
     * The method does not analyze the directed graph to run nodes in an "intelligent" way.
     */
    public void update() {
        for(Node n : nodes) {
            try {
                n.updateIfNotDirty();
            } catch(Exception e) {
                logger.debug(e.getMessage());
            }
        }
        for(NodeConnection c : connections) c.applyIfDirty();
        for(Node n : nodes) n.cleanAllOutputs();
    }

    /**
     * @return a {@link List} of all the {@link Node}s within this {@link NodeGraph}.
     * It is not a copy!  Use with caution.
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * @return a {@link List} of all the {@link NodeConnection}s within this {@link NodeGraph}.
     * It is not a copy!  Use with caution.
     */
    public List<NodeConnection> getConnections() {
        return connections;
    }

    /**
     * Adds a node to this graph.
     * @param node the subject
     * @return the same node for convenient method chaining.
     */
    public Node add(Node node) {
        nodes.add(node);
        return node;
    }

    /**
     * Remove a {@link Node} and all associated {@link NodeConnection}s from the model.
     * @param n the subject to be removed.
     */
    public void remove(Node n) {
        nodes.remove(n);
        removeConnectionsToNode(n);
    }

    /**
     * Adds a {@link NodeConnection} without checking if it already exists.
     * @param connection the item to add.
     * @return the same connection for convenient method chaining.
     */
    public NodeConnection add(NodeConnection connection) {
        connections.add(connection);
        return connection;
    }

    /**
     * Remove one {@link NodeConnection} from this graph.
     * @param c the item to remove.
     */
    public void remove(NodeConnection c) {
        connections.remove(c);
    }

    /**
     * Add all {@link Node}s and {@link NodeConnection}s from one model to this model.
     * @param nodeGraph the model to add.
     */
    public void add(NodeGraph nodeGraph) {
        if(nodeGraph==null) throw new IllegalArgumentException("nodeGraph cannot be null.");
        assignNewUniqueIDs(0);
        nodeGraph.assignNewUniqueIDs(Node.getUniqueIDSource());

        nodes.addAll(nodeGraph.nodes);
        connections.addAll(nodeGraph.connections);

        bumpUpIndexableID();
    }

    public void remove(NodeGraph nodeGraph) {
        if(nodeGraph==null) throw new IllegalArgumentException("nodeGraph cannot be null.");
        nodes.removeAll(nodeGraph.nodes);
        connections.removeAll(nodeGraph.connections);
    }


    /**
     * Remove all {@link NodeConnection}s from the model associated with a given {@link Node}
     * @param n the subject from which all connections should be removed.
     */
    public void removeConnectionsToNode(Node n) {
        ArrayList<NodeConnection> toKeep = new ArrayList<>();
        for(NodeConnection c : connections) {
            if(!c.isConnectedTo(n)) toKeep.add(c);
        }
        connections.clear();
        connections.addAll(toKeep);
    }

    /**
     * Searches this {@link NodeGraph} for an equivalent {@link NodeConnection}.
     * @param connection the item to match.
     * @return returns the matching {@link NodeConnection} or null.
     */
    public NodeConnection getMatchingConnection(NodeConnection connection) {
        for(NodeConnection c : connections) {
            if(c.equals(connection)) return c;
        }
        return null;
    }

    @Override
    public String toString() {
        return "NodeBasedEditorModel{" +
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
     * @param r radius limit
     * @return a {@link NodeConnectionPointInfo} describing the point found or null.
     */
    public NodeConnectionPointInfo getFirstNearbyConnection(Point point, double r) {
        double rr=r*r;
        for(Node n : nodes) {
            for(int i = 0; i < n.getNumVariables(); ++i) {
                NodeVariable<?> v = n.getVariable(i);
                if(v.getHasInput() && v.getInPosition().distanceSq(point) < rr) {
                    return new NodeConnectionPointInfo(n,i, NodeConnectionPointInfo.IN);
                }
                if(v.getHasOutput() && v.getOutPosition().distanceSq(point) < rr) {
                    return new NodeConnectionPointInfo(n,i, NodeConnectionPointInfo.OUT);
                }
            }
        }
        return null;
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
     * Every {@link Node} and {@link NodeConnection} has a unique ID.  If the model has just been restored from a file
     * then the static unique ID will be wrong.  This method bumps the first available unique ID up to the largest value
     * found.  Then the next attempt to create a unique item will be safe.
     */
    public void bumpUpIndexableID() { //TODO THIS SHOULDN'T BE PUBLIC!
        int id=0;
        for(Node n : nodes) {
            id = Math.max(id, n.getUniqueID());
        }
        Node.setUniqueIDSource(id);
    }

    /**
     * Find and remove any {@link NodeConnection} that connects to the input side of a given {@link NodeVariable}.
     * @param outVariable the {@link NodeVariable} with an input to be isolated.
     */
    public void removeAllConnectionsInto(NodeVariable<?> outVariable) {
        connections.removeAll(getAllConnectionsInto(outVariable));
    }

    public List<NodeConnection> getAllConnectionsInto(NodeVariable<?> outVariable) {
        ArrayList<NodeConnection> list = new ArrayList<>();

        for(NodeConnection c : connections) {
            if(c.getOutVariable() == outVariable) list.add(c);
        }

        return list;
    }

    private void assignNewUniqueIDs(int startingIndex) {
        for(Node n : nodes) n.setUniqueID(++startingIndex);
    }

    /**
     * Returns a deep copy of this {@link NodeGraph} by using the JSON serialization methods.
     * @return the {@link NodeGraph} copy
     */
    public NodeGraph deepCopy() {
        NodeGraph copy = new NodeGraph();
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
        if(nodes.size()==0) return null;

        Rectangle r=new Rectangle(nodes.get(0).getRectangle());
        for(Node n : nodes) {
            r.union(n.getRectangle());
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
        parseAllNodeConnectionsFromJSON(jo.getJSONArray("connections"));
        bumpUpIndexableID();
    }

    private void parseAllNodesFromJSON(JSONArray arr) throws JSONException {
        for (Object element : arr) {
            JSONObject o = (JSONObject)element;
            Node n = NodeFactory.createNode(o.getString("name"));
            n.parseJSON(o);
            add(n);
        }
    }

    private void parseAllNodeConnectionsFromJSON(JSONArray arr) throws JSONException {
        for (Object o : arr) {
            NodeConnection c = new NodeConnection();
            parseOneConnectionFromJSON(c, (JSONObject)o);
            add(c);
        }
    }

    /**
     * {@link NodeConnection} must be parsed in the {@link NodeGraph} because only here can we access the list of
     * nodes to find the one with a matching {@code getUniqueName()}.
     * @param c the connection to parse into.
     * @param jo the JSON to parse.
     */
    private void parseOneConnectionFromJSON(NodeConnection c, JSONObject jo) {
        if(jo.has("inNode")) {
            Node n = findNodeWithUniqueName(jo.getString("inNode"));
            int i = jo.getInt("inVariableIndex");
            c.setInput(n,i);
        }
        if(jo.has("outNode")) {
            Node n = findNodeWithUniqueName(jo.getString("outNode"));
            int i = jo.getInt("outVariableIndex");
            c.setOutput(n,i);
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
        for (NodeConnection c : connections) {
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

    public void setAllDirty() {
        for(Node n : nodes) {
            n.setAllDirty();
        }
    }

    /**
     * Returns a list of connections that connect to the selected {@link Node}s and unselected {@link Node}s.
     * @param selectedNodes the set of {@link Node}s.
     * @return a list of connections that connect to the selected {@link Node}s and unselected {@link Node}s.
     */
    public List<NodeConnection> getExteriorConnections(List<Node> selectedNodes) {
        return getConnectionsCounted(selectedNodes,1);
    }

    /**
     * Returns a list of connections that connect between the selected {@link Node}s.
     * @param selectedNodes the set of {@link Node}s.
     * @return a list of connections that connect between the selected {@link Node}s.
     */
    public List<NodeConnection> getInteriorConnections(List<Node> selectedNodes) {
        return getConnectionsCounted(selectedNodes,2);
    }

    /**
     * Returns a list of connections that have exactly <code>count</code> connections to the selected {@link Node}s.
     * @param selectedNodes the set of {@link Node}s.
     * @param count the number of connections to match.
     * @return a list of connections that have exactly <code>count</code> connections to the selected {@link Node}s.
     */
    private List<NodeConnection> getConnectionsCounted(List<Node> selectedNodes,int count) {
        ArrayList<NodeConnection> found = new ArrayList<>();

        for(NodeConnection c : getConnections()) {
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
}
