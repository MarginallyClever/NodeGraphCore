package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * C=atan2(y,x)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class ATan2 extends Node {
    private final NodeVariable<Number> a = NodeVariable.newInstance("X",Number.class,0,true,false);
    private final NodeVariable<Number> b = NodeVariable.newInstance("Y",Number.class,0,true,false);
    private final NodeVariable<Number> c = NodeVariable.newInstance("output",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public ATan2() {
        super("ATan2");
        addVariable(a);
        addVariable(b);
        addVariable(c);
    }

    @Override
    public void update() {
        double y = a.getValue().doubleValue();
        double x = b.getValue().doubleValue();
        c.setValue(Math.atan2(y,x));
        cleanAllInputs();
    }
}
