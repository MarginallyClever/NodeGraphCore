package com.marginallyclever.nodegraphcore.json;

import com.marginallyclever.nodegraphcore.DAO4JSON;
import org.json.JSONException;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class IntegerDAO4JSON implements DAO4JSON<Number> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        return value;
    }

    @Override
    public Integer fromJSON(Object object) throws JSONException {
        return (Integer)object;
    }
}