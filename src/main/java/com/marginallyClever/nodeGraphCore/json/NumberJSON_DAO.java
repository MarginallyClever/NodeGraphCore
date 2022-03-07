package com.marginallyClever.nodeGraphCore.json;

import org.json.JSONException;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NumberJSON_DAO implements JSON_DAO<Number> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        Number s = (Number)value;
        return s;
    }

    @Override
    public Number fromJSON(Object object) throws JSONException {
        Number s = (Number)object;
        return s;
    }
}