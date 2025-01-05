package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.dock.Output;
import com.marginallyclever.nodegraphcore.Node;

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
        addVariable(output);
    }

    @Override
    public void update() {
        tickCount++;
        output.send(tickCount);
    }

    @Override
    public void reset() {
        super.reset();
    }
}
