package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.DAORegistry;
import com.marginallyclever.nodegraphcore.NodeFactory;
import com.marginallyclever.nodegraphcore.DAO4JSONFactory;
import com.marginallyclever.nodegraphcore.NodeRegistry;
import com.marginallyclever.donatello.nodes.images.*;
import com.marginallyclever.donatello.nodes.images.blend.BlendDifference;
import com.marginallyclever.donatello.nodes.images.blend.BlendMultiply;
import com.marginallyclever.donatello.nodes.images.blend.BlendScreen;

import java.awt.image.BufferedImage;

/**
 * Registers Swing {@link com.marginallyclever.nodegraphcore.Node}s to the {@link NodeFactory}.
 * Registers Swing types with the JSON DAO factory.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class SwingRegistry implements NodeRegistry, DAORegistry {
    /**
     * Perform the registration.
     */
    public void registerNodes() {
        NodeFactory.registerNode(new LoadImage());
        NodeFactory.registerNode(new PrintImage());
        NodeFactory.registerNode(new ScaleImage());
        NodeFactory.registerNode(new BlendDifference());
        NodeFactory.registerNode(new BlendMultiply());
        NodeFactory.registerNode(new BlendScreen());
        NodeFactory.registerNode(new SplitToCMYK());
    }

    /**
     * Perform the registration.
     */
    @Override
    public void registerDAO() {
        DAO4JSONFactory.registerDAO(BufferedImage.class,new BufferedImage_DAO4JSON());
    }
}
