package com.marginallyclever.nodegraphcore.json;

import com.marginallyclever.nodegraphcore.DAO4JSON;
import org.json.JSONException;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class BooleanDAO4JSON implements DAO4JSON<Boolean> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        return (Boolean)value;
    }

    @Override
    public Boolean fromJSON(Object object) throws JSONException {
        return (Boolean)object;
    }
}