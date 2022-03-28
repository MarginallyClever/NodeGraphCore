package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.SupergraphInput;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * {@link SupergraphInput} for a {@link String}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadString extends Node implements SupergraphInput {
    private final NodeVariable<String> v = NodeVariable.newInstance("value",String.class,"",false,true);

    /**
     * Constructor for subclasses to call.
     */
    public LoadString() {
        super("LoadString");
        addVariable(v);
    }

    @Override
    public void update() {}
}
