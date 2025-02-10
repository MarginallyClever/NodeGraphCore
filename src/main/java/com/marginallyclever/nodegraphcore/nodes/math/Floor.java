package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * whole number = floor(decimal number)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Floor extends Node {
    private final Input<Number> a = new Input<>("decimal",Number.class,0);
    private final Output<Integer> c = new Output<>("whole number",Integer.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Floor() {
        super("Floor");
        addPort(a);
        addPort(c);
    }

    @Override
    public void update() {
        c.send(a.getValue().intValue());
    }
}
