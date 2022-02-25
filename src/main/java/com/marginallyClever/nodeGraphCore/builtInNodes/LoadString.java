package com.marginallyClever.nodeGraphCore.builtInNodes;

import com.marginallyClever.nodeGraphCore.SupergraphInput;
import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;

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

    /**
     * Constructor that sets a starting value
     * @param startingValue the starting value.
     */
    public LoadString(String startingValue) {
        this();
        v.setValue(startingValue);
    }

    @Override
    public Node create() {
        return new LoadString();
    }

    @Override
    public void update() {}
}
