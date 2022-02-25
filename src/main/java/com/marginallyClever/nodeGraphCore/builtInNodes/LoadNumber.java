package com.marginallyClever.nodeGraphCore.builtInNodes;

import com.marginallyClever.nodeGraphCore.SupergraphInput;
import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;

/**
 * {@link SupergraphInput} for a {@link Number}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadNumber extends Node implements SupergraphInput {
    private final NodeVariable<Number> v = NodeVariable.newInstance("value",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public LoadNumber() {
        super("LoadNumber");
        addVariable(v);
    }

    /**
     * Constructor for subclasses to call.
     * @param startingValue the starting value.
     */
    public LoadNumber(Number startingValue) {
        this();
        v.setValue(startingValue);
    }

    @Override
    public Node create() {
        return new LoadNumber();
    }

    @Override
    public void update() {}
}
