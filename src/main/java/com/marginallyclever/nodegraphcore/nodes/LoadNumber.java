package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

/**
 * {@link SupergraphInput} for a {@link Number}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadNumber extends Node implements SupergraphInput {
    private final Input<Number> value = new Input<>("value",Number.class,0);
    private final Input<Integer> qty = new Input<>("qty",Integer.class,1);
    private final Output<Number> output = new Output<>("output",Number.class,0);
    private boolean done=false;

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
