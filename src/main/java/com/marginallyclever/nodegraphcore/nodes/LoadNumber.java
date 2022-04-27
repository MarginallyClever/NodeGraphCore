package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;

/**
 * {@link SupergraphInput} for a {@link Number}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadNumber extends Node implements SupergraphInput {
    private final DockShipping<Number> value = new DockShipping<>("value",Number.class,0);
    private final DockReceiving<Integer> qty = new DockReceiving<>("qty",Integer.class,1);
    private final DockShipping<Number> output = new DockShipping<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public LoadNumber() {
        super("LoadNumber");
        addVariable(value);
        addVariable(qty);
        addVariable(output);
    }

    @Override
    public void update() {
        int q = qty.getValue();
        if(q!=0 && value.outputHasRoom()) {
            if(q>0) qty.setValue(q-1);
            output.send(new Packet<>(value.getValue()));
        }
    }
}
