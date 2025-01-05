package com.marginallyclever.nodegraphcore.nodes.routing;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

/**
 * Blocks in from going to output until the key is triggered.
 * @author Dan Royer
 * @since 2022-03-25
 */
public class Gate extends Node {
    private final Input<Object> a = new Input<>("in",Object.class,null);
    private final Input<Object> b = new Input<>("key",Object.class,null);
    private final Output<Object> c = new Output<>("output",Object.class,null);

    public Gate() {
        super("Gate");
        addVariable(a);
        addVariable(b);
        addVariable(c);
    }

    @Override
    public void update() {
        c.send(a.getValue());
    }
}
