package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * output=cos(A)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Cos extends Node {
    private final NodeVariable<Number> a = NodeVariable.newInstance("A",Number.class,0,true,false);
    private final NodeVariable<Number> b = NodeVariable.newInstance("output",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public Cos() {
        super("Cos");
        addVariable(a);
        addVariable(b);
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        b.setValue(Math.cos(av));
        cleanAllInputs();
    }
}
