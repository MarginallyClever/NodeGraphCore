package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.DAO4JSONFactory;
import com.marginallyClever.nodeGraphCore.NodeFactory;

import java.util.*;
import java.util.List;

/**
 * Convenient methods for dealing with system properties.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class PropertiesHelper {
    public static void showProperties() {
        System.out.println("------------------------------------------------");
        Properties p = System.getProperties();
        List<String> names = new ArrayList<>(p.stringPropertyNames());
        Collections.sort(names);
        for (String name : names) {
            System.out.println( name +" = "+ p.get(name));
        }
        System.out.println("------------------------------------------------");
    }

    public static void listAllNodes() {
        String add = "Nodes: ";
        for (String n : NodeFactory.getNames()) {
            System.out.print(add + n);
            add = ", ";
        }
        System.out.println(".");
    }

    public static void listAllDAO() {
        String add="DAOs: ";
        for(String n : DAO4JSONFactory.getNames()) {
            System.out.print(add + n);
            add = ", ";
        }
        System.out.println(".");
    }
}
