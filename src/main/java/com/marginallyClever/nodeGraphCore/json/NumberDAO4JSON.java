package com.marginallyClever.nodeGraphCore.json;

import com.marginallyClever.nodeGraphCore.DAO4JSON;
import org.json.JSONException;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NumberDAO4JSON implements DAO4JSON<Number> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        return value;
    }

    @Override
    public Number fromJSON(Object object) throws JSONException {
        return (Number)object;
    }
}