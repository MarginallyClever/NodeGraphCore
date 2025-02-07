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
    private final Output<String> output = new Output<>("output",String.class,"");

    /**
     * Constructor for subclasses to call.
     */
    public LoadString() {
        super("LoadString");
        addVariable(value);
        addVariable(output);
    }

    @Override
    public void update() {
        output.send(value.getValue());
    }
}
