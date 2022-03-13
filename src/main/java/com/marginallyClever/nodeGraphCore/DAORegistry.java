package com.marginallyClever.nodeGraphCore;

/**
 * NodeRegistry is a Service that can load {@link JSON_DAO}s into the local {@link JSON_DAO_Factory}.
 * It should be declared in the <b>module-info.java</b> as
 * <pre>
 * provides com.marginallyClever.nodeGraphCore.DAORegistry
 *          with your.implementing.class;</pre>
 * See also <a href='https://docs.oracle.com/javase/9/docs/api/java/util/ServiceLoader.html'>Deploying service providers as modules</a>
 * @author Dan Royer
 * @since 2022-03-12
 */
public interface DAORegistry {
    void registerDAO();
}
