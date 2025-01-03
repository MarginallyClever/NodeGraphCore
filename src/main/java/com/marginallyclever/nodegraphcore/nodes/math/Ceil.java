package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

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
        if(!a.hasPacketWaiting()) return;
        a.receive();
        c.send(new Packet<>(Math.ceil(a.getValue().doubleValue())));
    }
}
