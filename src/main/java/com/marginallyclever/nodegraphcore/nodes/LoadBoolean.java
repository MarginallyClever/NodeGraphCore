package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

import javax.swing.*;
import java.util.Objects;

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

    @Override
    public Icon getIcon() {
        return new ImageIcon(Objects.requireNonNull(LoadBoolean.class.getResource("icons8-boolean-48.png")));
    }
}
