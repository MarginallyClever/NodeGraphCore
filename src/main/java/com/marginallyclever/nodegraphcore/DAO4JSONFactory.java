package com.marginallyclever.nodegraphcore;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Maintains a map of Classes and their {@link DAO4JSON}.
 * Can convert a class to and from a JSONObject.
 *
 * @author Dan Royer
 * @since 2022-03-07
 */
public class DAO4JSONFactory {
    private static final Map<Class<?>, DAO4JSON<?>> daoRegistry = new HashMap<>();

    /**
     * Does not allow {@link DAO4JSON} to be registered more than once.
     * @param aClass one instance of the class.
     */
    public static void registerDAO(DAO4JSON<?> dao) {
        if(!daoRegistry.containsKey(dao.getClassType())) {
            daoRegistry.put(dao.getClassType(),dao);
        }
    }

    /**
     * Returns a value converted to JSON
     * @param aClass the class of the value
     * @param object the value
     * @return a value converted to JSON
     * @throws JSONException
     */
    public static Object toJSON(Class<?> aClass,Object object) throws JSONException {
        DAO4JSON<?> dao = daoRegistry.get(aClass);
        if(dao==null) throw new JSONException("no DAO for "+aClass.getName());
        return dao.toJSON(object);
    }

    /**
     * Returns a value converted from JSON
     * @param aClass the class of the value
     * @param object the JSON
     * @return a value converted from JSON
     * @throws JSONException
     */
    public static Object fromJSON(Class<?> aClass,Object object) throws JSONException {
        DAO4JSON<?> dao = daoRegistry.get(aClass);
        if(dao==null) {
            throw new JSONException("no DAO for "+aClass.getName());
        }
        return dao.fromJSON(object);
    }

    /**
     * unregisters all DAO.
     */
    public static void clear() {
        daoRegistry.clear();
    }

    public static void loadRegistries() {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        loadRegistries(helper.getExtensionClassLoader());
    }


    public static void loadRegistries(ClassLoader classLoader) {
        ServiceLoader<DAORegistry> serviceLoader = ServiceLoader.load(DAORegistry.class, classLoader);
        for (DAORegistry registry : serviceLoader) {
            registry.registerDAO();
        }
    }

    public static String[] getNames() {
        Set<Class<?>> list = daoRegistry.keySet();
        String [] names = new String [list.size()];
        int i=0;
        for( Class<?> c : list) {
            names[i++] = c.getName();
        }
        return names;
    }
}
