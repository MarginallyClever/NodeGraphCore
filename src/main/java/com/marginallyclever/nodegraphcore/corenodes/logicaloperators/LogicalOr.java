package com.marginallyclever.nodegraphcore.corenodes.logicaloperators;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * C=(A || B)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LogicalOr extends Node {
    private final NodeVariable<Boolean> a = NodeVariable.newInstance("A",Boolean.class,false,true,false);
    private final NodeVariable<Boolean> b = NodeVariable.newInstance("B",Boolean.class,false,true,false);
    private final NodeVariable<Boolean> c = NodeVariable.newInstance("output",Boolean.class,false,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public LogicalOr() {
        super("LogicalOr");
        addVariable(a);
        addVariable(b);
        addVariable(c);
    }

    /**
     * Constructor for subclasses to call.
     * @param a the starting value.
     * @param b the starting value.
     */
    public LogicalOr(double a, double b) {
        this();
        this.a.setValue(a);
        this.b.setValue(b);
    }

    @Override
    public Node create() {
        return new LogicalOr();
    }

    @Override
    public void update() {
        boolean av = a.getValue();
        boolean bv = b.getValue();
        c.setValue(av || bv);
        cleanAllInputs();
    }
}
