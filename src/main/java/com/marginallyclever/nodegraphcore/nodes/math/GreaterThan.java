package com.marginallyclever.nodegraphcore.nodes.math;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.port.Output;

import javax.swing.*;
import java.util.Objects;

/**
 * C = (A&gt;B) ? 1 : 0
 * @author Dan Royer
 * @since 2022-03-19
 */
public class GreaterThan extends Node {
    private final Input<Number> a = new Input<>("A",Number.class,0);
    private final Input<Number> b = new Input<>("B",Number.class,0);
    private final Output<Number> c = new Output<>("output",Number.class,0);

    /**
     * Constructor for subclasses to call.
     */
    public GreaterThan() {
        super("GreaterThan");
        addPort(a);
        addPort(b);
        addPort(c);
    }

    @Override
    public void update() {
        double av = a.getValue().doubleValue();
        double bv = b.getValue().doubleValue();
        c.setValue((av > bv) ? 1 : 0);
    }

    @Override
    public Icon getIcon() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("icons8-more-than-16.png")));
    }
}
