package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;

/**
 * Generate a new random number in the range max-min when updated.
 */
public class Random extends Node {
    private final DockReceiving<Number> vMax = new DockReceiving<>("max",Number.class,0);
    private final DockReceiving<Number> vMin = new DockReceiving<>("min",Number.class,0);
    private final DockShipping<Number> v = new DockShipping<>("value",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Random() {
        super("Random");
        addVariable(vMax);
        addVariable(vMin);
        addVariable(v);
    }

    @Override
    public void update() {
        if(0==countReceivingConnections()) return;
        if(!vMax.hasPacketWaiting() && !vMin.hasPacketWaiting()) return;
        vMin.receive();
        vMax.receive();
        double a = vMin.getValue().doubleValue();
        double b = vMax.getValue().doubleValue();
        v.send(new Packet<>(Math.random()*(b-a) + a));
    }
}
