package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * output=cos(A)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Cos extends Node {
    private final Input<Number> a = new Input<>("A",Number.class,0);
    private final Output<Number> c = new Output<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Cos() {
        super("Cos");
        addPort(a);
        addPort(c);
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        c.setValue(Math.cos(av));
    }
}
