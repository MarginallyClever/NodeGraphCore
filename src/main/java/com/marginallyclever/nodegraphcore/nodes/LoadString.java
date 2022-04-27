package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;

/**
 * {@link SupergraphInput} for a {@link String}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadString extends Node implements SupergraphInput {
    private final DockReceiving<String> value = new DockReceiving<>("value",String.class,"");
    private final DockReceiving<Integer> qty = new DockReceiving<>("qty",Integer.class,1);
    private final DockShipping<String> output = new DockShipping<>("output",String.class,"");
    private boolean done=false;

    /**
     * Constructor for subclasses to call.
     */
    public LoadString() {
        super("LoadString");
        addVariable(value);
        addVariable(qty);
        addVariable(output);
    }

    @Override
    public void update() {
        if(done) return;

        int q = qty.getValue();
        if(q!=0 && output.outputHasRoom()) {
            for(int i=0;i<q;++i) {
                output.send(new Packet<>(value.getValue()));
            }
            done=true;
        }
    }

    @Override
    public void reset() {
        super.reset();
        done=false;
    }
}
