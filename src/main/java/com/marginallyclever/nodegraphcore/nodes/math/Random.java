package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * Generate a new random number in the range max-min when updated.
 */
public class Random extends Node {
    private final Input<Number> vMax = new Input<>("max",Number.class,0);
    private final Input<Number> vMin = new Input<>("min",Number.class,0);
    private final Output<Number> v = new Output<>("value",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Random() {
        super("Random");
        addPort(vMax);
        addPort(vMin);
        addPort(v);
    }

    @Override
    public void update() {
        double a = vMin.getValue().doubleValue();
        double b = vMax.getValue().doubleValue();
        v.send(Math.random()*(b-a) + a);
    }
}
