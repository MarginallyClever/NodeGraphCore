package com.marginallyClever.nodeGraphCore.json;

import com.marginallyClever.nodeGraphCore.JSON_DAO;
import org.json.JSONException;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class ObjectJSON_DAO implements JSON_DAO<Object> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        return value;
    }

    @Override
    public Object fromJSON(Object object) throws JSONException {
        return object;
    }
}