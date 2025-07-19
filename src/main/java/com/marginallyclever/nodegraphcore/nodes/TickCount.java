package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.port.Output;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import java.util.Objects;

/**
 * Counts the number of ticks since the program started.
 * @author Dan Royer
 * @since 2023-09-27
 */
public class TickCount extends Node {
    private final Output<Number> output = new Output<>("output",Number.class,0);
    private long tickCount=0;

    /**
     * Constructor for subclasses to call.
     */
    public TickCount() {
        super("TickCount");
        addPort(output);
    }

    @Override
    public void update() {
        tickCount++;
        output.setValue(tickCount);
    }

    @Override
    public void reset() {
        super.reset();
    }



    @Override
    public Icon getIcon() {
        return new ImageIcon(Objects.requireNonNull(TickCount.class.getResource("icons8-stopwatch-48.png")));
    }
}
