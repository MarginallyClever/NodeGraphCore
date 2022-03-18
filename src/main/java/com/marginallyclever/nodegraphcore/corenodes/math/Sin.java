package com.marginallyclever.nodegraphcore.corenodes.math;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * output=sin(A)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Sin extends Node {
    private final NodeVariable<Number> a = NodeVariable.newInstance("A",Number.class,0,true,false);
    private final NodeVariable<Number> b = NodeVariable.newInstance("output",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public Sin() {
        super("Sin");
        addVariable(a);
        addVariable(b);
    }

    /**
     * Constructor for subclasses to call.
     * @param a the starting value.
     */
    public Sin(double a) {
        this();
        this.a.setValue(a);
    }

    @Override
    public Node create() {
        return new Sin();
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        b.setValue(Math.sin(av));
        cleanAllInputs();
    }
}
