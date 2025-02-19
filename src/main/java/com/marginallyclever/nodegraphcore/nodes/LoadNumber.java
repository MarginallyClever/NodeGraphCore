package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

/**
 * {@link Node} for a {@link Number}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadNumber extends Node {
    private final Input<Number> value = new Input<>("value",Number.class,0);
    private final Output<Number> output = new Output<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public LoadNumber() {
        super("LoadNumber");
        addPort(value);
        addPort(output);
    }

    public LoadNumber(Number number) {
        this();
        value.setValue(number);
    }

    @Override
    public void update() {
        output.setValue(value.getValue());
    }
}
