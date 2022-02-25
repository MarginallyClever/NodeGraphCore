package com.marginallyClever.nodeGraphCore.builtInNodes;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;
import com.marginallyClever.nodeGraphCore.SupergraphInput;

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

    /**
     * Constructor that sets a starting value
     * @param startingValue the starting value.
     */
    public LoadBoolean(Boolean startingValue) {
        this();
        v.setValue(startingValue);
    }

    @Override
    public Node create() {
        return new LoadBoolean();
    }

    @Override
    public void update() {}
}
