package com.marginallyclever.nodegraphcore.scheduler;

public class MultiplyNode extends Node {
    private final int multiplier;

    public MultiplyNode(int multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    protected Object process(Object input) {
        if (input instanceof Integer) {
            var result = (Integer) input * multiplier;
            System.out.println("Node ("+ input + " x " + multiplier + "): " + result);
            return result;
        }
        return null;
    }
}