package com.marginallyclever.nodegraphcore.corenodes;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

/**
 * sends A as a string to <pre>System.out.println()</pre>.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class PrintToStdOut extends Node {
    private final NodeVariable<Object> a = NodeVariable.newInstance("A",Object.class,null,true,false);

    /**
     * Constructor for subclasses to call.
     */
    public PrintToStdOut() {
        super("PrintToStdOut");
        addVariable(a);
    }

    /**
     * Constructor that sets a starting value
     * @param startingValue the starting value.
     */
    public PrintToStdOut(Object startingValue) {
        this();
        this.a.setValue(startingValue);
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
