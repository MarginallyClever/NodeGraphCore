package com.marginallyClever.nodeGraphCore;

import com.marginallyClever.nodeGraphCore.builtInNodes.LoadNumber;
import com.marginallyClever.nodeGraphCore.builtInNodes.LoadString;
import com.marginallyClever.nodeGraphCore.builtInNodes.PrintToStdOut;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.*;

/**
 *
 */
public class BuiltInNodeRegistry {
    /**
     * Call this once to register all the built-in nodes that are included in the package.
     */
    public static void registerNodes() {
        NodeFactory.registerNode(new LoadNumber());
        NodeFactory.registerNode(new Random());
        NodeFactory.registerNode(new Add());
        NodeFactory.registerNode(new Subtract());
        NodeFactory.registerNode(new Multiply());
        NodeFactory.registerNode(new Divide());
        NodeFactory.registerNode(new PrintToStdOut());
        NodeFactory.registerNode(new Cos());
        NodeFactory.registerNode(new Sin());
        NodeFactory.registerNode(new Tan());
        NodeFactory.registerNode(new ATan2());
        NodeFactory.registerNode(new Min());
        NodeFactory.registerNode(new Max());

        NodeFactory.registerNode(new LoadString());
    }
}
