package com.marginallyClever.nodeGraphCore;

/**
 * {@link Node}s with this interface can draw using the Swing's {@link java.awt.Graphics} context.
 * TODO don't drag Swing into the model!
 * @author Dan Royer
 * @since 2022-02-01
 */
public interface PrintWithGraphics {
    /**
     * Called when the implementing class should paint.
     * @param g the {@link java.awt.Graphics} context.
     */
    void print(java.awt.Graphics g);
}
