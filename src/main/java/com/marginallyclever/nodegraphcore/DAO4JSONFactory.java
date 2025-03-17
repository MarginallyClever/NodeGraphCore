package com.marginallyclever.nodegraphcore;

import org.json.JSONException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Maintains a map of Classes and their {@link DAO4JSON}.
 * Can convert a class to and from a JSONObject.
 *
 * @author Dan Royer
 * @since 2022-03-07
 */
public class DAO4JSONFactory {
    private static final Logger logger = LoggerFactory.getLogger(DAO4JSONFactory.class);

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
        if(dao==null) {
            throw new JSONException("no DAO for "+aClass.getName());
        }
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

    public static void registerAllDAOInPackage(String packageName) throws GraphException {
        logger.info("Registering all DAO in package: " + packageName);
        // Use Reflections to find all subtypes of Node in the package
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends DAO4JSON>> subTypes = reflections.getSubTypesOf(DAO4JSON.class);
        var list = new ArrayList<>();
        subTypes.stream().sorted(Comparator.comparing(Class::getName)).forEach(typeFound ->{
            //if(!typeFound.getName().startsWith(packageName)) return;
            list.add(typeFound.getName().substring(packageName.length() + 1));
        });
        String str = packageName + " contains DAO " + Arrays.toString(list.toArray());
        logger.info(str);

        for (var typeFound : subTypes) {
            if(!typeFound.getName().startsWith(packageName)) continue;

            try {
                DAO4JSON<?> daoInstance = typeFound.getDeclaredConstructor().newInstance();
                registerDAO(daoInstance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new GraphException("Failed to register DAO: " + typeFound.getName(), e);
            }
        }
    }

    static int size() {
        return daoRegistry.size();
    }
}
