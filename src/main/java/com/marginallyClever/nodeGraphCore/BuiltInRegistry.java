package com.marginallyClever.nodeGraphCore;

import com.marginallyClever.nodeGraphCore.builtInNodes.LoadNumber;
import com.marginallyClever.nodeGraphCore.builtInNodes.LoadString;
import com.marginallyClever.nodeGraphCore.builtInNodes.PrintToStdOut;
import com.marginallyClever.nodeGraphCore.builtInNodes.logicalOperators.LogicalAnd;
import com.marginallyClever.nodeGraphCore.builtInNodes.logicalOperators.LogicalNot;
import com.marginallyClever.nodeGraphCore.builtInNodes.logicalOperators.LogicalOr;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.*;
import com.marginallyClever.nodeGraphCore.json.*;

import java.awt.*;

/**
 * Registers built-in {@link Node}s to the {@link NodeFactory}.
 * Registers built-in types with the JSON DAO factory.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class BuiltInRegistry {
    /**
     * Perform the registration.
     */
    public static void register() {
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

        NodeFactory.registerNode(new LogicalOr());
        NodeFactory.registerNode(new LogicalAnd());
        NodeFactory.registerNode(new LogicalNot());

        JSON_DAO_Factory.registerDAO(Rectangle.class, new RectangleJSON_DAO());
        JSON_DAO_Factory.registerDAO(String.class, new StringJSON_DAO());
        JSON_DAO_Factory.registerDAO(Number.class, new NumberJSON_DAO());
        JSON_DAO_Factory.registerDAO(Boolean.class, new BooleanJSON_DAO());
        JSON_DAO_Factory.registerDAO(Object.class, new ObjectJSON_DAO());
    }
}
