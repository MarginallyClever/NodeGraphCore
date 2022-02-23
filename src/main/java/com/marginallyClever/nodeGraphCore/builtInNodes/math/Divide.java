package com.marginallyClever.nodeGraphCore.builtInNodes.math;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;

public class Divide extends Node {
    private final NodeVariable<Number> a = NodeVariable.newInstance("A",Number.class,0,true,false);
    private final NodeVariable<Number> b = NodeVariable.newInstance("B",Number.class,0,true,false);
    private final NodeVariable<Number> c = NodeVariable.newInstance("output",Number.class,0,false,true);

    public Divide() {
        super("Divide");
        addVariable(a);
        addVariable(b);
        addVariable(c);
    }

    public Divide(double a,double b) {
        this();
        this.a.setValue(a);
        this.b.setValue(b);
    }

    @Override
    public Node create() {
        return new Divide();
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        double bv = b.getValue().doubleValue();
        if(bv==0) c.setValue(0);
        else c.setValue(av / bv);
        cleanAllInputs();
    }
}
