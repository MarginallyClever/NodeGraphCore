package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

/**
 * {@link SupergraphInput} for a {@link Boolean}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadBoolean extends Node implements SupergraphInput {
    private final Input<Boolean> value = new Input<>("value",Boolean.class,false);
    private final Input<Integer> qty = new Input<>("qty",Integer.class,1);
    private final Output<Boolean> output = new Output<>("output",Boolean.class,false);
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