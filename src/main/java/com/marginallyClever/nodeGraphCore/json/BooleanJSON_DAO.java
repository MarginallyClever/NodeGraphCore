package com.marginallyClever.nodeGraphCore.json;

import com.marginallyClever.nodeGraphCore.JSON_DAO;
import org.json.JSONException;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class BooleanJSON_DAO implements JSON_DAO<Boolean> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        return (Boolean)value;
    }

    @Override
    public Boolean fromJSON(Object object) throws JSONException {
        return (Boolean)object;
    }
}