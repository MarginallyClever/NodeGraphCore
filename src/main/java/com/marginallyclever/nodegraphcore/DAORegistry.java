package com.marginallyclever.nodegraphcore;

/**
 * NodeRegistry is a Service that can load {@link DAO4JSON}s into the local {@link DAO4JSONFactory}.
 * It should be declared in the <b>module-info.java</b> as
 * <pre>
 * provides com.marginallyclever.nodegraphcore.DAORegistry
 *          with your.implementing.class;</pre>
 * See also <a href='https://docs.oracle.com/javase/9/docs/api/java/util/ServiceLoader.html'>Deploying service providers as modules</a>
 * @author Dan Royer
 * @since 2022-03-12
 */
public interface DAORegistry {
    String getName();

    void registerDAO();
}
