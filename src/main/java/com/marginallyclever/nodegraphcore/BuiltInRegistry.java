package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.corenodes.LoadNumber;
import com.marginallyclever.nodegraphcore.corenodes.LoadString;
import com.marginallyclever.nodegraphcore.corenodes.PrintToStdOut;
import com.marginallyclever.nodegraphcore.corenodes.logicaloperators.LogicalAnd;
import com.marginallyclever.nodegraphcore.corenodes.logicaloperators.LogicalNot;
import com.marginallyclever.nodegraphcore.corenodes.logicaloperators.LogicalOr;
import com.marginallyclever.nodegraphcore.corenodes.math.*;
import com.marginallyclever.nodegraphcore.json.*;

import java.awt.*;

/**
 * Registers built-in {@link Node}s to the {@link NodeFactory}.
 * Registers built-in types with the JSON DAO factory.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class BuiltInRegistry implements NodeRegistry, DAORegistry {
    /**
     * Perform the registration.
     */
    public void registerNodes() {
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
        NodeFactory.registerNode(new Mod());
        NodeFactory.registerNode(new Floor());
        NodeFactory.registerNode(new Ceil());
        NodeFactory.registerNode(new Equals());
        NodeFactory.registerNode(new GreaterThan());
        NodeFactory.registerNode(new LessThan());

        NodeFactory.registerNode(new LoadString());

        NodeFactory.registerNode(new LogicalOr());
        NodeFactory.registerNode(new LogicalAnd());
        NodeFactory.registerNode(new LogicalNot());
    }

    /**
     * Perform the registration.
     */
    @Override
    public void registerDAO() {
        DAO4JSONFactory.registerDAO(Rectangle.class, new RectangleDAO4JSON());
        DAO4JSONFactory.registerDAO(String.class, new StringDAO4JSON());
        DAO4JSONFactory.registerDAO(Number.class, new NumberDAO4JSON());
        DAO4JSONFactory.registerDAO(Boolean.class, new BooleanDAO4JSON());
        DAO4JSONFactory.registerDAO(Object.class, new ObjectDAO4JSON());
    }
}
