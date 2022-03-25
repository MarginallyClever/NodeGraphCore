package com.marginallyclever.nodegraphcore.corenodes;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;
import com.marginallyclever.nodegraphcore.SupergraphInput;

/**
 * {@link SupergraphInput} for a {@link Boolean}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadBoolean extends Node implements SupergraphInput {
    private final NodeVariable<Boolean> v = NodeVariable.newInstance("value",Boolean.class,false,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public LoadBoolean() {
        super("LoadBoolean");
        addVariable(v);
    }

    @Override
    public void update() {}
}
