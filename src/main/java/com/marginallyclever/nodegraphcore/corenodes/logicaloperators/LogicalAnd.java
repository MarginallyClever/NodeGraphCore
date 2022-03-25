package com.marginallyclever.nodegraphcore.corenodes.logicaloperators;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * C=(A &amp;&amp; B)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LogicalAnd extends Node {
    private final NodeVariable<Boolean> a = NodeVariable.newInstance("A",Boolean.class,false,true,false);
    private final NodeVariable<Boolean> b = NodeVariable.newInstance("B",Boolean.class,false,true,false);
    private final NodeVariable<Boolean> c = NodeVariable.newInstance("output",Boolean.class,false,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public LogicalAnd() {
        super("LogicalAnd");
        addVariable(a);
        addVariable(b);
        addVariable(c);
    }

    @Override
    public void update() {
        boolean av = a.getValue();
        boolean bv = b.getValue();
        c.setValue(av && bv);
        cleanAllInputs();
    }
}
