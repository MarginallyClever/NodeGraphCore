package com.marginallyclever.nodegraphcore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NodeCategory {
    private final String name;
    private final Supplier<Node> supplier;
    private final List<NodeCategory> children = new ArrayList<>();
    private NodeCategory parent=null;

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
}
