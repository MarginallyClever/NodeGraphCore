package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * Generate a new random number in the range max-min when updated.
 */
public class Random extends Node {
    private final NodeVariable<Number> vMax = NodeVariable.newInstance("max",Number.class,0,true,false);
    private final NodeVariable<Number> vMin = NodeVariable.newInstance("min",Number.class,0,true,false);
    private final NodeVariable<Number> v = NodeVariable.newInstance("value",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     * @param top the maximum value, exclusive.
     * @param bottom the minimum value, inclusive.
     */
    public Random() {
        super("Random");
        addVariable(vMax);
        addVariable(vMin);
        addVariable(v);
    }

    @Override
    public void update() {
        double a = vMin.getValue().doubleValue();
        double b = vMax.getValue().doubleValue();
        v.setValue(Math.random()*(b-a) + a);
        cleanAllInputs();
    }
}
