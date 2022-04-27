package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;

/**
 * whole number = floor(decimal number)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Floor extends Node {
    private final DockReceiving<Number> a = new DockReceiving<>("decimal",Number.class,0);
    private final DockShipping<Number> c = new DockShipping<>("whole number",Number.class,0);

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
        if(!a.hasPacketWaiting()) return;
        a.receive();
        c.send(new Packet<>(Math.floor(a.getValue().doubleValue())));
    }
}
