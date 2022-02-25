package com.marginallyClever.nodeGraphCore.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.marginallyClever.nodeGraphCore.JSONHelper;
import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeConnection;
import com.marginallyClever.nodeGraphCore.NodeGraph;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ollie
 * @since 2022-02-23
 */
public class NodeGraphJsonAdapter implements JsonSerializer<NodeGraph>, JsonDeserializer<NodeGraph> {
    /**
     * Needed by Gson.
     */
    public static final Type nodeType = TypeToken.getParameterized(ArrayList.class, Node.class).getType();

    @Override
    public JsonElement serialize(NodeGraph nodeGraph, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonElement nodes = context.serialize(nodeGraph.getNodes(), nodeType);
        jsonObject.add("nodes", nodes);
        jsonObject.add("connections", JSONHelper.serializeNodeConnections(nodeGraph.getConnections()));
        return jsonObject;
    }

    @Override
    public NodeGraph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        NodeGraph nodeGraph = new NodeGraph();
        List<Node> nodes = context.deserialize(jsonObject.get("nodes"), nodeType);
        nodes.forEach(nodeGraph::add);
        JSONHelper.deserializeNodeConnections(jsonObject.get("connections"), nodeGraph);
        nodeGraph.bumpUpIndexableID();

        return nodeGraph;
    }
}
