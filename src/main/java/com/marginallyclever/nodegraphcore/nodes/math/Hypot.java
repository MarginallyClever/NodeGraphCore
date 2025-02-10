package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * The length of the hypotenuse C of a right triangle with sides A and B.
 */
public class Hypot extends Node {
    private final Input<Number> a = new Input<>("adjacent",Number.class,0);
    private final Input<Number> b = new Input<>("opposite",Number.class,0);
    private final Output<Number> c = new Output<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Hypot() {
        super("Hypot");
        addPort(a);
        addPort(b);
        addPort(c);
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        double bv = b.getValue().doubleValue();
        c.send(Math.hypot(av,bv));
    }
}
