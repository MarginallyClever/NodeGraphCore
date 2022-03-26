package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.corenodes.LoadNumber;
import com.marginallyclever.nodegraphcore.corenodes.LoadString;
import com.marginallyclever.nodegraphcore.corenodes.PrintToStdOut;
import com.marginallyclever.nodegraphcore.corenodes.logicaloperators.LogicalAnd;
import com.marginallyclever.nodegraphcore.corenodes.logicaloperators.LogicalNot;
import com.marginallyclever.nodegraphcore.corenodes.logicaloperators.LogicalOr;
import com.marginallyclever.nodegraphcore.corenodes.math.*;
import com.marginallyclever.nodegraphcore.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Registers built-in {@link Node}s to the {@link NodeFactory}.
 * Registers built-in types with the JSON DAO factory.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class BuiltInRegistry implements NodeRegistry, DAORegistry {
    private static final Logger logger = LoggerFactory.getLogger(BuiltInRegistry.class);

    /**
     * Perform the registration.
     */
    public void registerNodes() throws GraphException {
        logger.info("Registering core nodes");
        NodeFactory.registerAllNodesInPackage("com.marginallyclever.nodegraphcore.corenodes");
    }

    /**
     * Perform the registration.
     */
    @Override
    public void registerDAO() {
        logger.info("Registering core DAOs");
        DAO4JSONFactory.registerDAO(Rectangle.class, new RectangleDAO4JSON());
        DAO4JSONFactory.registerDAO(String.class, new StringDAO4JSON());
        DAO4JSONFactory.registerDAO(Number.class, new NumberDAO4JSON());
        DAO4JSONFactory.registerDAO(Boolean.class, new BooleanDAO4JSON());
        DAO4JSONFactory.registerDAO(Object.class, new ObjectDAO4JSON());
    }
}
