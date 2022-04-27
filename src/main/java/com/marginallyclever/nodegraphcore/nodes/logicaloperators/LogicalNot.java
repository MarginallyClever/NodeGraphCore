package com.marginallyclever.nodegraphcore.nodes.logicaloperators;

import com.marginallyclever.nodegraphcore.*;

/**
 * C=(!A)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LogicalNot extends Node {
    private final DockReceiving<Boolean> a = new DockReceiving<>("A",Boolean.class,false);
    private final DockShipping<Boolean> c = new DockShipping<>("output",Boolean.class,false);

    /**
     * Constructor for subclasses to call.
     */
    public LogicalNot() {
        super("LogicalNot");
        addVariable(a);
        addVariable(c);
    }

    @Override
    public void update() {
        if(0==countReceivingConnections()) return;
        if(!a.hasPacketWaiting()) return;
        a.receive();
        boolean av = a.getValue();
        c.send(new Packet<>(!av));
    }
}
