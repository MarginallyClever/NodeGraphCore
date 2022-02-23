package com.marginallyClever.nodeGraphCore.builtInNodes;

import com.marginallyClever.nodeGraphCore.SupergraphInput;
import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;

public class LoadString extends Node implements SupergraphInput {
    private final NodeVariable<String> v = NodeVariable.newInstance("value",String.class,"",false,true);

    public LoadString() {
        super("LoadString");
        addVariable(v);
    }

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
