package com.marginallyclever.nodegraphcore.json;

import com.marginallyclever.nodegraphcore.AbstractDAO4JSON;
import org.json.JSONException;

import javax.annotation.Nonnull;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class BooleanDAO4JSON extends AbstractDAO4JSON<Boolean> {
    public BooleanDAO4JSON() {
        super(Boolean.class);
    }

    @Nonnull
    @Override
    public Object toJSON(Object value) throws JSONException {
        return value;
    }

    @Nonnull
    @Override
    public Boolean fromJSON(Object object) throws JSONException {
        return (Boolean)object;
    }
}