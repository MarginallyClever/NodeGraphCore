package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * whole number = floor(decimal number)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Floor extends Node {
    private final NodeVariable<Number> a = NodeVariable.newInstance("decimal",Number.class,0,true,false);
    private final NodeVariable<Number> c = NodeVariable.newInstance("whole number",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public Floor() {
        super("Floor");
        addVariable(a);
        addVariable(c);
    }

    @Override
    public void update() {
        c.setValue(Math.floor(a.getValue().doubleValue()));
        cleanAllInputs();
    }
}
