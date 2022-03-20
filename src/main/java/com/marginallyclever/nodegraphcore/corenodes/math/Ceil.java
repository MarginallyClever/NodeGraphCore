package com.marginallyclever.nodegraphcore.corenodes.math;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * whole number = ceil(decimal number)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Ceil extends Node {
    private final NodeVariable<Number> a = NodeVariable.newInstance("decimal",Number.class,0,true,false);
    private final NodeVariable<Number> c = NodeVariable.newInstance("whole number",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public Ceil() {
        super("Ceil");
        addVariable(a);
        addVariable(c);
    }

    /**
     * Constructor for subclasses to call.
     * @param y the starting value.
     * @param x the starting value.
     */
    public Ceil(double a) {
        this();
        this.a.setValue(a);
    }

    @Override
    public Node create() {
        return new Ceil();
    }

    @Override
    public void update() {
        c.setValue(Math.ceil(a.getValue().doubleValue()));
        cleanAllInputs();
    }
}
