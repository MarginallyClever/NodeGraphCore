package com.marginallyclever.nodegraphcore;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

/**
 * Maintains a map of {@link Node}s and their names.  Can create nodes on request, by name.
 * Can deliver a list of names.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NodeFactory {
    private static final Logger logger = LoggerFactory.getLogger(NodeFactory.class);

    private static final Map<String,Class<? extends Node>> nodeRegistry = new HashMap<>();

    private static final NodeCategory root = new NodeCategory("root");

    public static void registerAllNodesInPackage(String packageName) throws GraphException {
        // Use Reflections to find all subtypes of Node in the package
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Node>> subTypes = reflections.getSubTypesOf(Node.class);
        var list = new ArrayList<>();
        subTypes.stream().sorted(Comparator.comparing(Class::getName)).forEach(e->list.add(e.getName().substring(packageName.length()+1)));
        String str = packageName + " contains " + Arrays.toString(list.toArray());
        logger.info(str);

        for(var typeFound : subTypes) {
            registerNode(typeFound,packageName);
        }
    }

    public static void registerNode(Class<? extends Node> typeFound, String packageName) {
        verifyTypeNotRegistered(typeFound);
        verifyTypeConstructor(typeFound);
        addNodeToCategoryTree(typeFound,packageName);
    }

    private static void addNodeToCategoryTree(Class<? extends Node> typeFound,String packageName) {
        nodeRegistry.put(typeFound.getSimpleName(),typeFound);
        
        String fullName = typeFound.getName();
        if(!fullName.startsWith(packageName)) throw new RuntimeException("class "+fullName+" not part of package "+packageName);
        // get the novel part of the package name
        fullName = fullName.substring(packageName.length()+1);
        // split into each sub-package.
        var parts = fullName.split("\\.");
        // search the category tree for the nodes and add them as you go.
        System.out.println("Adding "+fullName);
        NodeCategory category = root;
        for(int i = 0 ; i < parts.length-1; i++) {
            String part = parts[i];
            NodeCategory next = category.getChildByName(part);
            if(next==null) {
                next = new NodeCategory(part,null);
                category.add(next);
            }
            category = next;
        }

        String part = parts[parts.length-1];
        var next = new NodeCategory(part, ()->{
            try {
                return typeFound.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                logger.error("Error creating NodeCategory instance", e);
                return null;
            }
        });
        category.add(next);
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
            try {
                for( Constructor<?> constructor : nodeRegistry.get(name).getDeclaredConstructors() ) {
                    if (constructor.getParameterCount() == 0) {
                        return (Node) constructor.newInstance();
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
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
        root.getChildren().clear();
    }

    /**
     * Initializes the {@link NodeFactory} by scanning the classpath for {@link Node}s.
     * Be sure to call {@link ServiceLoaderHelper#addFile(String)} before calling this method.
     * @throws Exception
     */
    public static void loadRegistries() {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        loadRegistries(helper.getExtensionClassLoader());
    }

    public static void loadRegistries(ClassLoader classLoader) {
        ServiceLoader<NodeRegistry> serviceLoader = ServiceLoader.load(NodeRegistry.class, classLoader);
        for (NodeRegistry registry : serviceLoader) {
            try {
                logger.info("Loading node registry: "+registry.getClass().getName());
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

    public static NodeCategory getRoot() {
        return root;
    }

    /**
     * find the sub-factory that matches the given identifier.
     * @param identifier the name of type of {@link Node} the sub-factory produces.
     * @return the sub-factory that matches the given identifier, or null if not found.
     */
    public static Supplier<Node> getSupplierFor(String identifier) {
        List<NodeCategory> toCheck = new ArrayList<>();
        toCheck.add(root);
        while(!toCheck.isEmpty()) {
            NodeCategory current = toCheck.removeFirst();
            if(current.getName().equals(identifier)) {
                return current.getSupplier();
            }
            toCheck.addAll(current.getChildren());
        }

        return null;
    }
}
