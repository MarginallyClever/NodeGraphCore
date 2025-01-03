package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

/**
 * Counts from start to end in increment sized steps, aka `for(i=start;i!=end;i+=increment)'
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Counter extends Node {
    private final Input<Integer> start = new Input<>("start",Integer.class,0);
    private final Input<Integer> end = new Input<>("end",Integer.class,1);
    private final Input<Integer> increment = new Input<>("increment",Integer.class,1);
    private final Output<Integer> output = new Output<>("output",Integer.class,0);
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
