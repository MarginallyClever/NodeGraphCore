package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;

/**
 * {@link SupergraphInput} for a {@link String}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadString extends Node implements SupergraphInput {
    private final DockShipping<String> v = new DockShipping<>("value",String.class,"");
    private final DockReceiving<Integer> qty = new DockReceiving<>("qty",Integer.class,1);

    /**
     * Constructor for subclasses to call.
     */
    public LoadString() {
        super("LoadString");
        addVariable(v);
        addVariable(qty);
    }

    @Override
    public void update() {
        int q = qty.getValue();
        if(q!=0 && v.outputHasRoom()) {
            if(q>0) qty.setValue(q-1);
            v.send(new Packet<>(v.getValue()));
        }
    }
}
