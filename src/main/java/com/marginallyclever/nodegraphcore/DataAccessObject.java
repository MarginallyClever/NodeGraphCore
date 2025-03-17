package com.marginallyclever.nodegraphcore;

/**
 * Base interface without any parameterization.
 * Avoids type erasure which confuses {@link org.reflections.Reflections}.
 */
public interface DataAccessObject {
}
