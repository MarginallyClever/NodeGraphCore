package com.marginallyclever.nodegraphcore;

/**
 * NodeRegistry is a Service that can load {@link Node}s into the local {@link NodeFactory}.
 * It should be declared in the <b>module-info.java</b> as
 * <pre>
 * provides com.marginallyclever.nodegraphcore.NodeRegistry
 *          with your.implementing.class;</pre>
 * See also <a href='https://docs.oracle.com/javase/9/docs/api/java/util/ServiceLoader.html'>Deploying service providers as modules</a>
 * @author Dan Royer
 * @since 2022-03-12
 */
public interface NodeRegistry {
    String getName();

    void registerNodes();
}
