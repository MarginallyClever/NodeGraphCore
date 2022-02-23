package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.NodeFactory;
import com.marginallyClever.nodeGraphSwing.nodes.images.LoadImage;
import com.marginallyClever.nodeGraphSwing.nodes.images.PrintImage;
import com.marginallyClever.nodeGraphSwing.nodes.turtle.*;

public class SwingNodeFactory {
    public static void registerSwingNodes() {
        NodeFactory.registerNode(new LoadImage());
        NodeFactory.registerNode(new PrintImage());

        NodeFactory.registerNode(new LoadTurtle());
        NodeFactory.registerNode(new PrintTurtle());
        NodeFactory.registerNode(new TurtleLine());
        NodeFactory.registerNode(new TurtleRectangle());
        NodeFactory.registerNode(new TurtleCircle());
        NodeFactory.registerNode(new TurtlePatternOnPath());
    }
}
