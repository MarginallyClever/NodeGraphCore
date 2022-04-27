package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;

/**
 * C=mod(A,B)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Mod extends Node {
    private final DockReceiving<Number> a = new DockReceiving<>("A",Number.class,0);
    private final DockReceiving<Number> b = new DockReceiving<>("B",Number.class,0);
    private final DockShipping<Number> c = new DockShipping<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Mod() {
        super("Mod");
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
        c.send(new Packet<>(av%bv));
    }
}
