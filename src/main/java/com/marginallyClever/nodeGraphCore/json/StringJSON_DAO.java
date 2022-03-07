package com.marginallyClever.nodeGraphCore.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class StringJSON_DAO implements JSON_DAO<String> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        String s = (String)value;
        return s;
    }

    @Override
    public String fromJSON(Object object) throws JSONException {
        String s = (String)object;
        return s;
    }
}