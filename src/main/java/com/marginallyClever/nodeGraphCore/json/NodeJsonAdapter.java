package com.marginallyClever.nodeGraphCore.json;

import com.google.gson.*;
import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeFactory;
import com.marginallyClever.nodeGraphCore.JSONHelper;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * @author Ollie
 * @since 2022-02-23
 */
public class NodeJsonAdapter implements JsonSerializer<Node>, JsonDeserializer<Node> {

    @Override
    public JsonElement serialize(Node node, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", node.getName());
        jsonObject.addProperty("uniqueID", node.getUniqueID());
        jsonObject.addProperty("label", node.getLabel());
        jsonObject.add("bounds", context.serialize(node.getRectangle(), Rectangle.class));
        jsonObject.add("variables", JSONHelper.serializeNodeVariables(node.getVariables()));
        return jsonObject;
    }

    @Override
    public Node deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();

        Node node = NodeFactory.createNode(name);
        if(node != null){
            node.setUniqueID(jsonObject.get("uniqueID").getAsInt());
            node.setLabel(jsonObject.has("label") ? jsonObject.get("label").getAsString() : "");
            node.setRectangle(context.deserialize(jsonObject.get("bounds"), Rectangle.class));
            JSONHelper.deserializeNodeVariables(node.getVariables(), jsonObject.get("variables"));
            return node;
        }
        return null;
    }
}
