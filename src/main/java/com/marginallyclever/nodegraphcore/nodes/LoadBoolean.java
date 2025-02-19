package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * Node for a {@link Boolean}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadBoolean extends Node  {
    private final Input<Boolean> value = new Input<>("value",Boolean.class,false);
    private final Output<Boolean> output = new Output<>("output",Boolean.class,false);

    /**
     * Constructor for subclasses to call.
     */
    public LoadBoolean() {
        super("LoadBoolean");
        addPort(value);
        addPort(output);
    }

    @Override
    public void update() {
        output.setValue(value.getValue());
    }
}
