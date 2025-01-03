package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

/**
 * output=sin(A)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Sin extends Node {
    private final Input<Number> a = new Input<>("A",Number.class,0);
    private final Output<Number> c = new Output<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Sin() {
        super("Sin");
        addVariable(a);
        addVariable(c);
    }

    @Override
    public void update() {
        if(!a.hasPacketWaiting()) return;
        a.receive();
        double av = a.getValue().doubleValue();
        c.send(new Packet<>(Math.sin(av)));
    }
}
