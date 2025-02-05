package com.marginallyclever.nodegraphcore.json;

import com.marginallyclever.nodegraphcore.AbstractDAO4JSON;
import org.json.JSONException;

/**
 * Convenience methods for serializing and de-serializing objects in this package.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class DoubleDAO4JSON extends AbstractDAO4JSON<Double> {
    public DoubleDAO4JSON() {
        super(Double.class);
    }

    @Override
    public Object toJSON(Object value) throws JSONException {
        return value;
    }

    @Override
    public Double fromJSON(Object object) throws JSONException {
        return (Double)object;
    }
}