package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.dock.Output;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Packet;

/**
 * Counts the number of ticks since the program started.
 * @author Dan Royer
 * @since 2023-09-27
 */
public class TickCount extends Node {
    private final Output<Integer> output = new Output<>("output",Integer.class,0);
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
        if(output.outputHasRoom()) {
            output.send(new Packet<>(tickCount));
        }
    }

    @Override
    public void reset() {
        super.reset();
    }
}
