package com.marginallyclever.nodegraphcore.json;

import com.marginallyclever.nodegraphcore.AbstractDAO4JSON;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class RectangleDAO4JSON extends AbstractDAO4JSON<Rectangle> {
    public RectangleDAO4JSON() {
        super(Rectangle.class);
    }

    @Nonnull
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

    @Nonnull
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