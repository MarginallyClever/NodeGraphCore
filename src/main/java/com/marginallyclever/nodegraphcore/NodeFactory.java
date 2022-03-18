package com.marginallyclever.nodegraphcore;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Maintains a map of {@link Node}s and their names.  Can create nodes on request, by name.
 * Can deliver a list of names.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NodeFactory {
    private static final Map<String,Node> nodeRegistry = new HashMap<>();

    /**
     * Does not allow nodes to be registered more than once.
     * @param n one instance of the node.
     */
    public static void registerNode(Node n) {
        if(!nodeRegistry.containsKey(n.getName())) {
            nodeRegistry.put(n.getName(), n);
        }
    }

    /**
     *
     * @param name The {@link Node} you want.
     * @return The {@link Node}
     * @throws IllegalArgumentException if the matchine {@link Node} cannot be found.
     */
    public static Node createNode(String name) throws IllegalArgumentException {
        if(nodeRegistry.containsKey(name)) {
            return nodeRegistry.get(name).create();
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

    public static void loadRegistries() {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        loadRegistries(helper.getExtensionClassLoader());
    }

    public static void loadRegistries(ClassLoader classLoader) {
        ServiceLoader<NodeRegistry> serviceLoader = ServiceLoader.load(NodeRegistry.class, classLoader);
        for (NodeRegistry registry : serviceLoader) {
            registry.registerNodes();
        }
    }
}
