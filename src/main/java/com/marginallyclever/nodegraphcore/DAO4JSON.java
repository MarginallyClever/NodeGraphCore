package com.marginallyclever.nodegraphcore;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nonnull;

/**
 * This interface describes a Data Access Object which converts an instance of a given class to and from JSON.
 */
public interface DAO4JSON<T> extends DataAccessObject {
    /**
     * @return the class which this DAO services.
     */
    @Nonnull
    Class<?> getClassType();

    /**
     * Returns a value converted to JSON.
     * @param value the thing of type T.
     * @return a value converted to JSON.
     * @throws JSONException if the value cannot be converted to JSON.
     */
    @Nonnull Object toJSON(Object value) throws JSONException;

    /**
     * Returns a value converted from JSON.
     * @param object the {@link JSONObject}
     * @return a value converted from JSON.
     * @throws JSONException if the object cannot be converted to the desired type.
     */
    @Nonnull T fromJSON(Object object) throws JSONException;
}
