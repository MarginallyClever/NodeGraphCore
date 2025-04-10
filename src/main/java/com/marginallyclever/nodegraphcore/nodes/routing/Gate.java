package com.marginallyclever.nodegraphcore.nodes.routing;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * Blocks in from going to output until the key is triggered.
 * @author Dan Royer
 * @since 2022-03-25
 */
public class Gate extends Node {
    private final Input<Object> a = new Input<>("in", Object.class, new Object());
    private final Input<Object> b = new Input<>("key", Object.class, new Object());
    private final Output<Object> c = new Output<>("output", Object.class, new Object());

    public Gate() {
        super("Gate");
        addPort(a);
        addPort(b);
        addPort(c);
    }

    @Override
    public void update() {
        c.setValue(a.getValue());
    }
}
