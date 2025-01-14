package com.marginallyclever.nodegraphcore.scheduler;

public class PrintNode extends Node {
    @Override
    protected Object process(Object input) {
        System.out.println("Output: " + input);
        return null;
    }
}