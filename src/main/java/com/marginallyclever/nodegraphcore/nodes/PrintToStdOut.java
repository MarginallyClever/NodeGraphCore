package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.DockReceiving;

/**
 * sends A as a string to <pre>System.out.println()</pre>.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class PrintToStdOut extends Node {
    private final DockReceiving<Object> a = new DockReceiving("A",Object.class,null);

    /**
     * Constructor for subclasses to call.
     */
    public PrintToStdOut() {
        super("PrintToStdOut");
        addVariable(a);
    }

    @Override
    public void update() {
        if(!a.hasPacketWaiting()) return;
        a.receive();
        Object var = a.getValue();
        String output = (var!=null) ? var.toString() : "null";
        System.out.println(getUniqueID()+": "+output);
    }
}
