package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

/**
 * C=atan2(y,x)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class ATan2 extends Node {
    private final Input<Number> a = new Input<>("X",Number.class,0);
    private final Input<Number> b = new Input<>("Y",Number.class,0);
    private final Output<Number> c = new Output<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public ATan2() {
        super("ATan2");
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
        double y = a.getValue().doubleValue();
        double x = b.getValue().doubleValue();
        c.send(new Packet<>(Math.atan2(y,x)));
    }
}
