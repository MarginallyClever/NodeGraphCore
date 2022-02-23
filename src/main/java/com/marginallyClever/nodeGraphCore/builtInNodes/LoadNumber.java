package com.marginallyClever.nodeGraphCore.builtInNodes;

import com.marginallyClever.nodeGraphCore.SupergraphInput;
import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;

public class LoadNumber extends Node implements SupergraphInput {
    private final NodeVariable<Number> v = NodeVariable.newInstance("value",Number.class,0,false,true);

    public LoadNumber() {
        super("LoadNumber");
        addVariable(v);
    }

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
