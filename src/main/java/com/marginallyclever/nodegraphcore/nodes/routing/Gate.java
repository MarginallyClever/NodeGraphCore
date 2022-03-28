package com.marginallyclever.nodegraphcore.nodes.routing;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * Blocks in from going to out until the key is triggered.
 * @author Dan Royer
 * @since 2022-03-25
 */
public class Gate extends Node {
    private final NodeVariable<Object> a = NodeVariable.newInstance("in",Object.class,null,true,false);
    private final NodeVariable<Object> b = NodeVariable.newInstance("key",Object.class,null,true,false);
    private final NodeVariable<Object> c = NodeVariable.newInstance("output",Object.class,null,false,true);

    public Gate() {
        super("Gate");
        addVariable(a);
        addVariable(b);
        addVariable(c);
    }

    @Override
    public void update() throws Exception {
        c.setValue(a.getValue());
    }
}
