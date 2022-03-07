package com.marginallyClever.nodeGraphCore.json;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a map of Classes and their {@link JSON_DAO}.
 * Can convert a class to and from a JSONObject.
 *
 * @author Dan Royer
 * @since 2022-03-07
 */
public class JSON_DAO_Factory {
    private static Map<Class, JSON_DAO<?>> daoRegistry = new HashMap<>();

    /**
     * Does not allow {@link JSON_DAO} to be registered more than once.
     * @param aClass one instance of the class.
     * @throws IllegalArgumentException if two {@link JSON_DAO} are registered with the same class.
     */
    public static void registerDAO(Class aClass, JSON_DAO<?> dao) throws IllegalArgumentException {
        if(daoRegistry.containsKey(aClass)) {
            throw new IllegalArgumentException("A node is already registered with the name "+aClass.getName());
        }
        daoRegistry.put(aClass,dao);
    }

    /**
     * Returns a value converted to JSON
     * @param aClass the class of the value
     * @param object the value
     * @return a value converted to JSON
     * @throws JSONException
     */
    public static Object toJSON(Class aClass,Object object) throws JSONException {
        return daoRegistry.get(aClass).toJSON(object);
    }

    /**
     * Returns a value converted from JSON
     * @param aClass the class of the value
     * @param object the JSON
     * @return a value converted from JSON
     * @throws JSONException
     */
    public static Object fromJSON(Class aClass,Object object) throws JSONException {
        return daoRegistry.get(aClass).fromJSON(object);
    }
}
