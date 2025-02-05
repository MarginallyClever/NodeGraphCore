package com.marginallyclever.nodegraphcore.json;

import com.marginallyclever.nodegraphcore.AbstractDAO4JSON;
import org.json.JSONException;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NumberDAO4JSON extends AbstractDAO4JSON<Number> {
    public NumberDAO4JSON() {
        super(Number.class);
    }

    @Override
    public Object toJSON(Object value) throws JSONException {
        return value;
    }

    @Override
    public Number fromJSON(Object object) throws JSONException {
        return (Number)object;
    }
}