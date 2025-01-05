package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

/**
 * whole number = floor(decimal number)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Floor extends Node {
    private final Input<Number> a = new Input<>("decimal",Number.class,0);
    private final Output<Number> c = new Output<>("whole number",Number.class,0);

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
        c.send(Math.floor(a.getValue().doubleValue()));
    }
}
