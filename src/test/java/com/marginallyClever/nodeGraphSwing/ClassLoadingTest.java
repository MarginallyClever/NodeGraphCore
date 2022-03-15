package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.*;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

public class ClassLoadingTest {
    @Test
    public void listAllNodes() {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        ClassLoader classLoader = helper.getExtensionClassLoader();
        ServiceLoader<NodeRegistry> loader = ServiceLoader.load(NodeRegistry.class, classLoader);
        for (NodeRegistry registry : loader) {
            registry.registerNodes();
        }
        String add = "Nodes: ";
        for (String n : NodeFactory.getNames()) {
            System.out.print(add + n);
            add = ", ";
        }
        System.out.println(".");
        NodeFactory.clear();
    }

    @Test
    public void listAllDAOs() {
        ServiceLoaderHelper helper = new ServiceLoaderHelper();
        ClassLoader classLoader = helper.getExtensionClassLoader();
        ServiceLoader<DAORegistry> loader = ServiceLoader.load(DAORegistry.class, classLoader);
        for (DAORegistry registry : loader) {
            registry.registerDAO();
        }
        String add="DAOs: ";
        for(String n : DAO4JSONFactory.getNames()) {
            System.out.print(add + n);
            add = ", ";
        }
        System.out.println(".");
        DAO4JSONFactory.clear();
    }
}
