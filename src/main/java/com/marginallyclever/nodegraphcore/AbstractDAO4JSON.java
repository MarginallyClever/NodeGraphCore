package com.marginallyclever.nodegraphcore;

public abstract class AbstractDAO4JSON<T> implements DAO4JSON<T> {
    private final Class<T> inst;

    public AbstractDAO4JSON(Class<T> inst) {
        this.inst = inst;
    }

    @Override
    public Class<?> getClassType() {
        return inst;
    }
}
