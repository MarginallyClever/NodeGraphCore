package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;

/**
 * {@link SupergraphInput} for a {@link Boolean}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadBoolean extends Node implements SupergraphInput {
    private final DockReceiving<Boolean> value = new DockReceiving<>("value",Boolean.class,false);
    private final DockReceiving<Integer> qty = new DockReceiving<>("qty",Integer.class,1);
    private final DockShipping<Boolean> output = new DockShipping<>("output",Boolean.class,false);
    private boolean done=false;

    /**
     * Constructor for subclasses to call.
     */
    public LoadBoolean() {
        super("LoadBoolean");
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
