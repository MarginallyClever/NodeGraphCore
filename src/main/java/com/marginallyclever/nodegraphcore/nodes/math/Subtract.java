package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

import javax.swing.*;
import java.util.Objects;

/**
 * A-B {@link Node}
 */
public class Subtract extends Node {
    private final Input<Number> a = new Input<>("A",Number.class,0);
    private final Input<Number> b = new Input<>("B",Number.class,0);
    private final Output<Number> c = new Output<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public Subtract() {
        super("Subtract");
        addPort(a);
        addPort(b);
        addPort(c);
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        double bv = b.getValue().doubleValue();
        c.setValue(av - bv);
    }

    @Override
    public Icon getIcon() {
        return new ImageIcon(Objects.requireNonNull(Subtract.class.getResource("icons8-subtract-16.png")));
    }
}
