package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers built-in {@link Node}s to the {@link NodeFactory}.
 * Registers built-in types with the JSON DAO factory.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class BuiltInRegistry implements NodeRegistry, DAORegistry {
    private static final Logger logger = LoggerFactory.getLogger(BuiltInRegistry.class);

    public String getName() {
        return "Built-in";
    }

    /**
     * Register nodes
     */
    public void registerNodes() throws GraphException {
        logger.info("Registering core nodes");
        NodeFactory.registerAllNodesInPackage("com.marginallyclever.nodegraphcore.nodes");
    }

    /**
     * Register DAOs.
     */
    @Override
    public void registerDAO() {
        logger.info("Registering core DAOs");
        DAO4JSONFactory.registerDAO(new RectangleDAO4JSON());
        DAO4JSONFactory.registerDAO(new StringDAO4JSON());
        DAO4JSONFactory.registerDAO(new DoubleDAO4JSON());
        DAO4JSONFactory.registerDAO(new IntegerDAO4JSON());
        DAO4JSONFactory.registerDAO(new NumberDAO4JSON());
        DAO4JSONFactory.registerDAO(new BooleanDAO4JSON());
        DAO4JSONFactory.registerDAO(new ObjectDAO4JSON());
    }
}
