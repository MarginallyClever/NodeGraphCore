package com.marginallyclever.nodegraphcore.corenodes.math;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * A*B {@link Node}
 */
public class Multiply extends Node {
    private final NodeVariable<Number> a = NodeVariable.newInstance("A",Number.class,0,true,false);
    private final NodeVariable<Number> b = NodeVariable.newInstance("B",Number.class,0,true,false);
    private final NodeVariable<Number> c = NodeVariable.newInstance("output",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public Multiply() {
        super("Multiply");
        addVariable(a);
        addVariable(b);
        addVariable(c);
    }

    /**
     * Constructor for subclasses to call.
     * @param a the starting value.
     * @param b the starting value.
     */
    public Multiply(double a,double b) {
        this();
        this.a.setValue(a);
        this.b.setValue(b);
    }

    @Override
    public Node create() {
        return new Multiply();
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        double bv = b.getValue().doubleValue();
        c.setValue(av * bv);
        cleanAllInputs();
    }
}
