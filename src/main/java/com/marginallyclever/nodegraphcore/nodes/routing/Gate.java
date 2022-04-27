package com.marginallyclever.nodegraphcore.nodes.routing;

import com.marginallyclever.nodegraphcore.*;

/**
 * Blocks in from going to output until the key is triggered.
 * @author Dan Royer
 * @since 2022-03-25
 */
public class Gate extends Node {
    private final DockReceiving<Object> a = new DockReceiving<>("in",Object.class,null);
    private final DockReceiving<Object> b = new DockReceiving<>("key",Object.class,null);
    private final DockShipping<Object> c = new DockShipping<>("output",Object.class,null);

    public Gate() {
        super("Gate");
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
        c.send(new Packet<>(a.getValue()));
    }
}
