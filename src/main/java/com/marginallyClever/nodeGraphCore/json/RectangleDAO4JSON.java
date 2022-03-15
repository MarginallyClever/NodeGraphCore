package com.marginallyClever.nodeGraphCore.json;

import com.marginallyClever.nodeGraphCore.DAO4JSON;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.Rectangle;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class RectangleDAO4JSON implements DAO4JSON<Rectangle> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        Rectangle rectangle = (Rectangle)value;
        JSONObject r = new JSONObject();
        r.put("x",rectangle.x);
        r.put("y",rectangle.y);
        r.put("width",rectangle.width);
        r.put("height",rectangle.height);
        return r;
    }

    @Override
    public Rectangle fromJSON(Object object) throws JSONException {
        Rectangle rect = new Rectangle();
        JSONObject r = (JSONObject)object;
        rect.x = r.getInt("x");
        rect.y = r.getInt("y");
        rect.width = r.getInt("width");
        rect.height = r.getInt("height");
        return rect;
    }
}