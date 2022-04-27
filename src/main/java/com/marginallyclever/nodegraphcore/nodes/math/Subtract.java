package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;

/**
 * A-B {@link Node}
 */
public class Subtract extends Node {
    private final DockReceiving<Number> a = new DockReceiving<>("A",Number.class,0);
    private final DockReceiving<Number> b = new DockReceiving<>("B",Number.class,0);
    private final DockShipping<Number> c = new DockShipping<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Subtract() {
        super("Subtract");
        addVariable(a);
        addVariable(b);
        addVariable(c);
    }

    @Override
    public void update() {
        if(0==countReceivingConnections()) return;
        if(!a.hasPacketWaiting() && !b.hasPacketWaiting()) return;
        a.receive();
        b.receive();
        double av = a.getValue().doubleValue();
        double bv = b.getValue().doubleValue();
        c.send(new Packet<>(av - bv));
    }
}
