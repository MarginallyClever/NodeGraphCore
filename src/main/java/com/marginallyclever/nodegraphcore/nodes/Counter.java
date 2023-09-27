package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;

/**
 * Counts from start to end in increment sized steps, aka `for(i=start;i!=end;i+=increment)'
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Counter extends Node {
    private final DockReceiving<Integer> start = new DockReceiving<>("start",Integer.class,0);
    private final DockReceiving<Integer> end = new DockReceiving<>("end",Integer.class,1);
    private final DockReceiving<Integer> increment = new DockReceiving<>("increment",Integer.class,1);
    private final DockShipping<Integer> output = new DockShipping<>("output",Integer.class,0);
    private boolean done=false;

    /**
     * Constructor for subclasses to call.
     */
    public Counter() {
        super("Counter");
        addVariable(start);
        addVariable(end);
        addVariable(increment);
        addVariable(output);
    }

    @Override
    public void update() {
        if(done) return;

        int s = start.getValue();
        int e = end.getValue();
        int add = increment.getValue();
        if(output.outputHasRoom()) {
            for(int i=s;i!=e;i+=add) {
                output.send(new Packet<>(i));
            }
            done=true;
        }
    }

    @Override
    public void reset() {
        super.reset();
        done=false;
    }
}
