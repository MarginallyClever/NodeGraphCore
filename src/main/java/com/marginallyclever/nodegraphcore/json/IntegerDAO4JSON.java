package com.marginallyclever.nodegraphcore.json;

import com.marginallyclever.nodegraphcore.AbstractDAO4JSON;
import org.json.JSONException;

import javax.annotation.Nonnull;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class IntegerDAO4JSON extends AbstractDAO4JSON<Integer> {
    public IntegerDAO4JSON() {
        super(Integer.class);
    }

    @Nonnull
    @Override
    public Object toJSON(Object value) throws JSONException {
        return value;
    }

    @Nonnull
    @Override
    public Integer fromJSON(Object object) throws JSONException {
        if(object instanceof Integer) return (Integer)object;
        if(object instanceof Number) return Integer.valueOf(object.toString());
        throw new JSONException("Invalid object type "+object.getClass());
    }
}