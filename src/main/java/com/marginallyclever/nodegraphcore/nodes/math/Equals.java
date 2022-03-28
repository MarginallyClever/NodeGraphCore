package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * C = (A==B) ? 1 : 0
 * @author Dan Royer
 * @since 2022-03-19
 */
public class Equals extends Node {
    private final NodeVariable<Number> a = NodeVariable.newInstance("A",Number.class,0,true,false);
    private final NodeVariable<Number> b = NodeVariable.newInstance("B",Number.class,0,true,false);
    private final NodeVariable<Number> c = NodeVariable.newInstance("output",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public Equals() {
        super("Equals");
        addVariable(a);
        addVariable(b);
        addVariable(c);
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        double bv = b.getValue().doubleValue();
        c.setValue((av == bv) ? 1 : 0);
        cleanAllInputs();
    }
}
