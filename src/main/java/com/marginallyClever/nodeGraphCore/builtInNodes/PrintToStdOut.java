package com.marginallyClever.nodeGraphCore.builtInNodes;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;

public class PrintToStdOut extends Node {
    private final NodeVariable<Object> a = NodeVariable.newInstance("A",Object.class,null,true,false);

    public PrintToStdOut() {
        super("PrintToStdOut");
        addVariable(a);
    }

    public PrintToStdOut(Object obj) {
        this();
        this.a.setValue(obj);
    }

    @Override
    public Node create() {
        return new PrintToStdOut();
    }

    @Override
    public void update() {
        if(!isDirty()) return;
        Object var = a.getValue();
        String output = (var!=null) ? var.toString() : "null";
        System.out.println(getUniqueID()+": "+output);
        cleanAllInputs();
    }
}
