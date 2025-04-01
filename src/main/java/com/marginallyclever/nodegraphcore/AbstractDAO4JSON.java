package com.marginallyclever.nodegraphcore;

import javax.annotation.Nonnull;

public abstract class AbstractDAO4JSON<T> implements DAO4JSON<T> {
    private final Class<T> inst;

    public AbstractDAO4JSON(@Nonnull Class<T> inst) {
        this.inst = inst;
    }

    @Override
    public @Nonnull Class<?> getClassType() {
        return inst;
    }
}
