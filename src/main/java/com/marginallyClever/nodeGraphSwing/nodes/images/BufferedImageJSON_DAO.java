package com.marginallyClever.nodeGraphSwing.nodes.images;

import com.marginallyClever.nodeGraphCore.JSON_DAO;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.image.BufferedImage;

public class BufferedImageJSON_DAO implements JSON_DAO<BufferedImage> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        BufferedImage image = (BufferedImage)value;
        JSONObject v = new JSONObject();
        v.put("width",image.getWidth());
        v.put("height",image.getHeight());
        v.put("type",image.getType());
        // for a complete snapshot, capture all the instance details, too.
        return v;
    }

    @Override
    public BufferedImage fromJSON(Object object) throws JSONException {
        JSONObject v = (JSONObject)object;
        int w = v.getInt("width");
        int h = v.getInt("height");
        int type = v.getInt("type");
        // for a complete snapshot, restore all the instance details, too.
        return new BufferedImage(w,h,type);
    }
}
