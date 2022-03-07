package com.marginallyClever.nodeGraphCore;

import org.json.JSONObject;

import java.awt.*;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Ollie
 * @since 2022-02-01
 */
public class JSONHelper {
    public static JSONObject rectangleToJSON(Rectangle rectangle) {
        JSONObject r = new JSONObject();
        r.put("x",rectangle.x);
        r.put("y",rectangle.y);
        r.put("width",rectangle.width);
        r.put("height",rectangle.height);
        return r;
    }

    public static Rectangle rectangleFromJSON(JSONObject r) {
        Rectangle rect = new Rectangle();
        rect.x = r.getInt("x");
        rect.y = r.getInt("y");
        rect.width = r.getInt("width");
        rect.height = r.getInt("height");
        return rect;
    }
}