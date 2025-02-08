package com.marginallyclever.nodegraphcore.nodes.logicaloperators;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * C=(!A)
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LogicalNot extends Node {
    private final Input<Boolean> a = new Input<>("A",Boolean.class,false);
    private final Output<Boolean> c = new Output<>("output",Boolean.class,false);

    /**
     * Constructor for subclasses to call.
     */
    public LogicalNot() {
        super("LogicalNot");
        addPort(a);
        addPort(c);
    }

    @Override
    public void update() {
        boolean av = a.getValue();
        c.send(!av);
    }
}
