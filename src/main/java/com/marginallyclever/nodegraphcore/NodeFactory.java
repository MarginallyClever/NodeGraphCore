package com.marginallyclever.nodegraphcore;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

/**
 * Maintains a map of {@link Node}s and their names.  Can create nodes on request, by name.
 * Can deliver a list of names.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NodeFactory {
    private static final Logger logger = LoggerFactory.getLogger(NodeFactory.class);
    private static final Map<String,Class<? extends Node>> nodeRegistry = new HashMap<>();

    public static void registerAllNodesInPackage(String packageName) throws GraphException {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> subTypes = reflections.get(SubTypes.of(Node.class).asClass());
        for(Class<?> typeFound : subTypes) {
            logger.debug("Registering node "+typeFound.getName());
            verifyTypeNotRegistered(typeFound);
            verifyTypeConstructor(typeFound);
            nodeRegistry.put(typeFound.getSimpleName(),(Class<? extends Node>) typeFound);
        }
    }

    private static void verifyTypeNotRegistered(Class<?> typeFound) throws GraphException {
        if(nodeRegistry.containsKey(typeFound.getSimpleName())) {
            throw new GraphException("Cannot register two nodes with name "+typeFound.getName());
        }
    }

    private static void verifyTypeConstructor(Class<?> typeFound) throws GraphException {
        boolean valid=false;
        for( Constructor<?> constructor : typeFound.getDeclaredConstructors() ) {
            if(constructor.getParameterCount()==0) {
                valid=true;
                break;
            }
        }
        if(!valid) throw new GraphException("Node "+typeFound.getName()+" must have public zero argument constructor.");
    }

    /**
     *
     * @param name The {@link Node} you want.
     * @return The {@link Node}
     * @throws IllegalArgumentException if the matchine {@link Node} cannot be found.
     */
    public static Node createNode(String name) throws IllegalArgumentException {
        if(nodeRegistry.containsKey(name)) {
            Node n = null;
            try {
                n = (Node)nodeRegistry.get(name).getDeclaredConstructors()[0].newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            return n;
        }
        throw new IllegalArgumentException("Node type not found: "+name);
    }

    /**
     * Returns an array containing the unique names of every {@link Node} registered.
     * @return an array containing the unique names of every {@link Node} registered.
     */
    public static String [] getNames() {
        Set<String> list = nodeRegistry.keySet();
        return list.stream().sorted().toArray(String[]::new);
    }

    /**
     * Unregisters everything.
     */
    public static void clear() {
        nodeRegistry.clear();
    }

    public static void loadRegistries() throws Exception {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        loadRegistries(helper.getExtensionClassLoader());
    }

    public static void loadRegistries(ClassLoader classLoader) throws Exception {
        ServiceLoader<NodeRegistry> serviceLoader = ServiceLoader.load(NodeRegistry.class, classLoader);
        for (NodeRegistry registry : serviceLoader) {
            try {
                registry.registerNodes();
            } catch(NoSuchMethodError e) {
                logger.warn("Plugin out of date: {}", registry.getClass().getName());
                // TODO which plugin?
            }
        }
    }

    /**
     * Returns true if the nodeName is registered.
     * @param nodeName the name to find.
     * @return true if the nodeName is registered.
     */
    public static boolean knowsAbout(String nodeName) {
        for(String s : getNames()) {
            if(s.equals(nodeName)) return true;
        }
        return false;
    }
}
