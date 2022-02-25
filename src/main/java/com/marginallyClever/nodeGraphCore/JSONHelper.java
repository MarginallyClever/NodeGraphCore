package com.marginallyClever.nodeGraphCore;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.marginallyClever.nodeGraphCore.json.NodeGraphJsonAdapter;
import com.marginallyClever.nodeGraphCore.json.NodeJsonAdapter;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Ollie
 * @since 2022-02-01
 */
public class JSONHelper {
    private static GsonBuilder builder;
    private static Gson gson;

    /**
     * Returns the default {@link Gson}.
     * @return the default {@link Gson}.
     */
    public static Gson getDefaultGson() {
        if(gson == null) {
            gson = getDefaultGsonBuilder().create();
        }
        return gson;
    }

    /**
     * Returns the default {@link GsonBuilder}.
     * @return the default {@link GsonBuilder}.
     */
    public static GsonBuilder getDefaultGsonBuilder() {
        if(builder == null){
            builder = new GsonBuilder();
            builder.setPrettyPrinting();
            registerTypeAdapters(builder);
        }
        return builder;
    }

    /**
     * Registers the NodeGraph classes with a {@link GsonBuilder}.
     * @param builder the {@link GsonBuilder}.
     */
    public static void registerTypeAdapters(GsonBuilder builder) {
        builder.registerTypeAdapter(NodeGraph.class, new NodeGraphJsonAdapter());
        builder.registerTypeHierarchyAdapter(Node.class, new NodeJsonAdapter());
    }

    /**
     * Creates a deep copy of the given {@link NodeGraph} using json serialization/deserialization
     * @param graph the {@link NodeGraph} to copy
     * @return the {@link NodeGraph} copy
     */
    public static NodeGraph deepCopy(NodeGraph graph){
        JsonElement jsonElement = JSONHelper.getDefaultGson().toJsonTree(graph);
        return JSONHelper.getDefaultGson().fromJson(jsonElement, NodeGraph.class);
    }

    /**
     * Creates a deep copy of the given {@link Node} using json serialization/deserialization
     * @param source the {@link NodeGraph} to copy
     * @return the {@link NodeGraph} copy
     */
    public static Node deepCopy(Node source){
        JsonElement jsonElement = JSONHelper.getDefaultGson().toJsonTree(source);
        return JSONHelper.getDefaultGson().fromJson(jsonElement, Node.class);
    }

    /**
     * Used for serializing a {@link Collection} of {@link NodeConnection}s to a {@link JsonElement}
     * @param nodeConnections the {@link Collection} of {@link NodeConnection}s to serialize
     * @return a {@link JsonElement} representing the list of {@link NodeConnection}s
     */
    public static JsonElement serializeNodeConnections(Collection<NodeConnection> nodeConnections){
        JsonArray jsonArray = new JsonArray();
        for(NodeConnection connection : nodeConnections){
            jsonArray.add(serializeNodeConnection(connection));
        }
        return jsonArray;
    }

    /**
     * Used for serializing a {@link Collection} of {@link NodeConnection}s from a {@link JsonElement}
     * @param jsonElement the {@link JsonElement} to read the {@link Collection} of {@link NodeConnection}s from
     * @param dstGraph the destination {@link NodeGraph}, where the connections will be added
     */
    public static void deserializeNodeConnections(JsonElement jsonElement, NodeGraph dstGraph){
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for(JsonElement e : jsonArray) {
            dstGraph.add(deserializeNodeConnection(e, dstGraph));
        }
    }

    /**
     * Used for serializing a single {@link NodeConnection} to a {@link JsonElement}
     * @param connection the {@link NodeConnection} to serialize
     * @return a {@link JsonElement} representing the {@link NodeConnection}
     */
    public static JsonElement serializeNodeConnection(NodeConnection connection){
        JsonObject jsonObject = new JsonObject();
        if(connection.getInNode() != null) {
            jsonObject.addProperty("inNode", connection.getInNode().getUniqueName());
            jsonObject.addProperty("inVariableIndex", connection.getInVariableIndex());
        }
        if(connection.getOutNode() != null) {
            jsonObject.addProperty("outNode", connection.getOutNode().getUniqueName());
            jsonObject.addProperty("outVariableIndex", connection.getOutVariableIndex());
        }
        return jsonObject;
    }

    /**
     * Used for de-serializing a single {@link NodeConnection} from a {@link JsonElement}.
     * This method only performs the deserialization and doesn't add the connection to the graph, that is handled by {@link #deserializeNodeConnections}
     *
     * @param jsonElement the {@link JsonElement} to de-serialize the {@link NodeConnection} from
     * @param dstGraph the destination {@link NodeGraph}
     * @return the {@link NodeConnection}
     */
    public static NodeConnection deserializeNodeConnection(JsonElement jsonElement, NodeGraph dstGraph){
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        NodeConnection connection = new NodeConnection();

        if(jsonObject.has("inNode")) {
            Node n = dstGraph.findNodeWithUniqueName(jsonObject.get("inNode").getAsString());
            int i = jsonObject.get("inVariableIndex").getAsInt();
            connection.setInput(n,i);
        }
        if(jsonObject.has("outNode")) {
            Node n = dstGraph.findNodeWithUniqueName(jsonObject.get("outNode").getAsString());
            int i = jsonObject.get("outVariableIndex").getAsInt();
            connection.setOutput(n,i);
        }

        return connection;
    }

    /**
     * Used for serializing a {@link Collection} of {@link NodeVariable}s to a {@link JsonElement}
     * @param variables the {@link Collection} of {@link NodeVariable}s to serialize
     * @return a {@link JsonElement} representing the {@link Collection} of {@link NodeVariable}
     */
    public static JsonElement serializeNodeVariables(Collection<NodeVariable<?>> variables){
        JsonArray jsonArray = new JsonArray();
        for(NodeVariable<?> variable : variables){
            jsonArray.add(serializeNodeVariable(variable));
        }
        return jsonArray;
    }

    /**
     * Used for de-serializing a {@link Collection} of {@link NodeVariable}s to a {@link JsonElement}
     * @param variables the {@link Collection} of {@link NodeVariable}s to deserialize into, MUST contain all of the {@link NodeVariable}s required already.
     * @param jsonElement the element to de-serialize the nodes from
     */
    public static void deserializeNodeVariables(Collection<NodeVariable<?>> variables, JsonElement jsonElement){
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        for(JsonElement e : jsonArray){
            JsonObject jsonObject = e.getAsJsonObject();
            String variableName = jsonObject.get("name").getAsString();

            NodeVariable<?> variable = null;

            for(NodeVariable<?> v : variables){
                if(v.getName().equals(variableName)){
                    variable = v;
                    break;
                }
            }

            if(variable == null){
                continue;
            }
            deserializeNodeVariable(variable, e);
        }
    }

    /**
     * Used for serializing a single {@link NodeVariable} to a {@link JsonElement}
     * @param variable the {@link NodeVariable}s to serialize
     * @return a {@link JsonElement} representing the {@link NodeVariable}
     */
    public static JsonElement serializeNodeVariable(NodeVariable<?> variable){
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("value", getDefaultGson().toJsonTree(variable.getValue(), variable.getTypeClass()));
        jsonObject.addProperty("name", variable.getName());
        jsonObject.addProperty("hasInput", variable.getHasInput());
        jsonObject.addProperty("hasOutput", variable.getHasOutput());
        jsonObject.add("bounds", getDefaultGson().toJsonTree(variable.getRectangle(), Rectangle.class));
        jsonObject.addProperty("isDirty", variable.getIsDirty());
        return jsonObject;
    }

    /**
     * Used for de-serializing a single {@link NodeVariable} from a {@link JsonElement}
     * @param variable the {@link NodeVariable}s to deserialize into
     * @param jsonElement the element to de-serialize the nodes from
     */
    public static void deserializeNodeVariable(NodeVariable<?> variable, JsonElement jsonElement){
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        variable.setValue(getDefaultGson().fromJson(jsonObject.get("value"), variable.getTypeClass())); //important: set the value first, otherwise isDirty will be overwritten

        variable.name = jsonObject.get("name").getAsString();
        variable.hasInput = jsonObject.get("hasInput").getAsBoolean();
        variable.hasOutput = jsonObject.get("hasOutput").getAsBoolean();
        variable.rectangle.setBounds(getDefaultGson().fromJson(jsonObject.get("bounds"), Rectangle.class));
        variable.isDirty = jsonObject.get("isDirty").getAsBoolean();
    }

}