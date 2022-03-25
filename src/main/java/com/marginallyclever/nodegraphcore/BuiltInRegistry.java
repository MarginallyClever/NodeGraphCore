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
    public void registerNodes() throws GraphException {
        NodeFactory.registerAllNodesInPackage("com.marginallyclever.nodegraphcore.corenodes");
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
