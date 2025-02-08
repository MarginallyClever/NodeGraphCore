package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * C=mod(A,B)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Mod extends Node {
    private final Input<Number> a = new Input<>("A",Number.class,0);
    private final Input<Number> b = new Input<>("B",Number.class,0);
    private final Output<Number> c = new Output<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Mod() {
        super("Mod");
        addPort(a);
        addPort(b);
        addPort(c);
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        double bv = b.getValue().doubleValue();
        c.send(av%bv);
    }
}
