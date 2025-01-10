package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * whole number = ceil(decimal number)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Ceil extends Node {
    private final Input<Number> a = new Input<>("decimal",Number.class,0);
    private final Output<Number> c = new Output<>("whole number",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Ceil() {
        super("Ceil");
        addVariable(a);
        addVariable(c);
    }

    @Override
    public void update() {
        c.send(Math.ceil(a.getValue().doubleValue()));
    }
}
