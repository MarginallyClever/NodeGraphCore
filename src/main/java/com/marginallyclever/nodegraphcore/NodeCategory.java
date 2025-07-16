package com.marginallyclever.nodegraphcore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A category of {@link Node} that can be used to organize nodes into a tree structure.
 * Each category can have a parent and children, allowing for hierarchical organization.
 * Categories can also provide a supplier to create instances of the node type they represent.
 */
public class NodeCategory {
    private final String name;
    private final Supplier<Node> supplier;
    private final List<NodeCategory> children = new ArrayList<>();
    private NodeCategory parent=null;

    public NodeCategory(String name) {
        this(name,null);
    }

    public NodeCategory(String name,Supplier<Node> supplier) {
        this.name = name;
        this.supplier = supplier;
    }

    public void add(NodeCategory c) {
        children.add(c);
        c.parent = this;
    }

    public NodeCategory add(String name, Supplier<Node> supplier) {
        NodeCategory item = new NodeCategory(name,supplier);
        add(item);
        return item;
    }

    public String getName() {
        return name;
    }

    public NodeCategory getParent() {
        return parent;
    }

    public List<NodeCategory> getChildren() {
        return children;
    }

    public Supplier<Node> getSupplier() {
        return supplier;
    }

    /**
     *
     * @param name the name to match.
     * @return first child with matching name, or null.
     */
    public NodeCategory getChildByName(String name) {
        if(name==null) throw new IllegalArgumentException("Name cannot be null");
        for(var child : children) {
            if(name.equals(child.getName())) return child;
        }
        return null;
    }
}
