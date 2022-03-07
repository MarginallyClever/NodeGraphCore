package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.NodeFactory;
import com.marginallyClever.nodeGraphCore.json.JSON_DAO_Factory;
import com.marginallyClever.nodeGraphSwing.nodes.images.*;
import com.marginallyClever.nodeGraphSwing.nodes.images.blend.BlendDifference;
import com.marginallyClever.nodeGraphSwing.nodes.images.blend.BlendMultiply;
import com.marginallyClever.nodeGraphSwing.nodes.images.blend.BlendScreen;

import java.awt.image.BufferedImage;

/**
 * Registers Swing {@link com.marginallyClever.nodeGraphCore.Node}s to the {@link NodeFactory}.
 * Registers Swing types with the JSON DAO factory.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class SwingRegistry {
    /**
     * Perform the registration.
     */
    public static void register() {
        NodeFactory.registerNode(new LoadImage());
        NodeFactory.registerNode(new PrintImage());
        NodeFactory.registerNode(new ScaleImage());
        NodeFactory.registerNode(new BlendDifference());
        NodeFactory.registerNode(new BlendMultiply());
        NodeFactory.registerNode(new BlendScreen());

        JSON_DAO_Factory.registerNode(BufferedImage.class,new BufferedImageJSON_DAO());
    }
}
