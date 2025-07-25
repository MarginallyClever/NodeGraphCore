package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

import javax.swing.*;
import java.util.Objects;

/**
 * {@link Node} for a {@link String}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class LoadString extends Node {
    private final Input<String> value = new Input<>("value",String.class,"");
    private final Output<String> output = new Output<>("output",String.class,"");

    /**
     * Constructor for subclasses to call.
     */
    public LoadString() {
        super("LoadString");
        addPort(value);
        addPort(output);
    }

    @Override
    public void update() {
        output.setValue(value.getValue());
    }

    @Override
    public Icon getIcon() {
        return new ImageIcon(Objects.requireNonNull(LoadString.class.getResource("icons8-string-48.png")));
    }
}
