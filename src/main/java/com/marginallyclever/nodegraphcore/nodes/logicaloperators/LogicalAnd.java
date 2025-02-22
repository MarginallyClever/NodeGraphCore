package com.marginallyclever.nodegraphcore.nodes.logicaloperators;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * C=(A &amp;&amp; B)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LogicalAnd extends Node {
    private final Input<Boolean> a = new Input<>("A",Boolean.class,false);
    private final Input<Boolean> b = new Input<>("B",Boolean.class,false);
    private final Output<Boolean> c = new Output<>("output",Boolean.class,false);

    /**
     * Constructor for subclasses to call.
     */
    public LogicalAnd() {
        super("LogicalAnd");
        addPort(a);
        addPort(b);
        addPort(c);
    }

    @Override
    public void update() {
        boolean av = a.getValue();
        boolean bv = b.getValue();
        c.setValue(av && bv);
    }
}
