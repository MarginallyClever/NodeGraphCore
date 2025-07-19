package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.port.Input;

import javax.swing.*;
import java.util.Objects;

/**
 * sends A as a string to <pre>System.out.println()</pre>.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class PrintToStdOut extends Node {
    private final Input<Object> a = new Input<>("A", Object.class, new Object());

    /**
     * Constructor for subclasses to call.
     */
    public PrintToStdOut() {
        super("PrintToStdOut");
        addPort(a);
    }

    @Override
    public void update() {
        Object var = a.getValue();
        String output = (var!=null) ? var.toString() : "null";
        System.out.println(getUniqueID()+": "+output);
    }

    @Override
    public Icon getIcon() {
        return new ImageIcon(Objects.requireNonNull(PrintToStdOut.class.getResource("icons8-print-48.png")));
    }
}
