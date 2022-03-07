package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.NodeFactory;
import com.marginallyClever.nodeGraphSwing.nodes.images.Difference;
import com.marginallyClever.nodeGraphSwing.nodes.images.LoadImage;
import com.marginallyClever.nodeGraphSwing.nodes.images.PrintImage;

/**
 * This registry adds Swing {@link com.marginallyClever.nodeGraphCore.Node}s to the user's menu via the
 * {@link NodeFactory}.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class SwingNodeRegistry {
    /**
     * Registers Swing {@link com.marginallyClever.nodeGraphCore.Node}s for the user menu.
     */
    public static void registerNodes() {
        NodeFactory.registerNode(new LoadImage());
        NodeFactory.registerNode(new PrintImage());
        NodeFactory.registerNode(new Difference());
    }
}
