package com.marginallyclever.nodegraphcore.nodes.logicaloperators;

import com.marginallyclever.nodegraphcore.*;

/**
 * C=(A &amp;&amp; B)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LogicalAnd extends Node {
    private final DockReceiving<Boolean> a = new DockReceiving<>("A",Boolean.class,false);
    private final DockReceiving<Boolean> b = new DockReceiving<>("B",Boolean.class,false);
    private final DockShipping<Boolean> c = new DockShipping<>("output",Boolean.class,false);

    /**
     * Constructor for subclasses to call.
     */
    public LogicalAnd() {
        super("LogicalAnd");
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
        boolean av = a.getValue();
        boolean bv = b.getValue();
        c.send(new Packet<>(av && bv));
    }
}
