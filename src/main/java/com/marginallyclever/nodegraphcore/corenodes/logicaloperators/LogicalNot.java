package com.marginallyclever.nodegraphcore.corenodes.logicaloperators;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * C=(!A)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LogicalNot extends Node {
    private final NodeVariable<Boolean> a = NodeVariable.newInstance("A",Boolean.class,false,true,false);
    private final NodeVariable<Boolean> c = NodeVariable.newInstance("output",Boolean.class,false,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public LogicalNot() {
        super("LogicalNot");
        addVariable(a);
        addVariable(c);
    }

    /**
     * Constructor for subclasses to call.
     * @param a the starting value.
     */
    public LogicalNot(double a) {
        this();
        this.a.setValue(a);
    }

    @Override
    public Node create() {
        return new LogicalNot();
    }

    @Override
    public void update() {
        boolean av = a.getValue();
        c.setValue(!av);
        cleanAllInputs();
    }
}
