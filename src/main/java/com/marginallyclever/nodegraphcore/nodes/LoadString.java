package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * {@link SupergraphInput} for a {@link String}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadString extends Node implements SupergraphInput {
    private final Input<String> value = new Input<>("value",String.class,"");
    private final Input<Integer> qty = new Input<>("qty",Integer.class,1);
    private final Output<String> output = new Output<>("output",String.class,"");
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
        if(q>0) {
            output.send(value.getValue());
            q--;
            qty.setValue(q);
            if(q<=0) done=true;
        }
    }

    @Override
    public void reset() {
        super.reset();
        done=false;
    }
}
