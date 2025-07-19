package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.nodes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Registers built-in {@link Node}s to the {@link NodeFactory}.
 * Registers built-in types with the JSON DAO factory.</p>
 * <p>Do not instantiate this class or call these directly.  Instead call <code>NodeFactory.loadRegistries()</code> and <code>DAO4JSONFactory.loadRegistries()</code></p>
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
    @Override
    public void registerNodes() {
        logger.info("Registering core nodes");
        //NodeFactory.registerAllNodesInPackage("com.marginallyclever.nodegraphcore.nodes");
        // manually register nodes to avoid loading the test nodes.
        NodeFactory.registerNode(InterpretJava.class, "com.marginallyclever.nodegraphcore.nodes");
        NodeFactory.registerNode(LoadBoolean.class, "com.marginallyclever.nodegraphcore.nodes");
        NodeFactory.registerNode(LoadNumber.class, "com.marginallyclever.nodegraphcore.nodes");
        NodeFactory.registerNode(LoadString.class, "com.marginallyclever.nodegraphcore.nodes");
        NodeFactory.registerNode(PrintToStdOut.class, "com.marginallyclever.nodegraphcore.nodes");
        NodeFactory.registerNode(TickCount.class, "com.marginallyclever.nodegraphcore.nodes");
    }

    /**
     * Register DAOs.
     */
    @Override
    public void registerDAO() {
        logger.info("Registering core DAOs");
        DAO4JSONFactory.registerAllDAOInPackage("com.marginallyclever.nodegraphcore.json");
    }
}
