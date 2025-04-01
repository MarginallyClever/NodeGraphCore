package com.marginallyclever.nodegraphcore.port;

import com.marginallyclever.nodegraphcore.Connection;

import javax.annotation.Nonnull;

/**
 * An {@link Input} is a {@link Port} that can receive data from a {@link Connection}.
 * @param <T> the type of data this {@link Input} receives.
 */
public class Input<T> extends Port<T> {
    private Connection from;

    protected final T defaultValue;

    public Input(String name, Class<T> type, T startingValue) throws IllegalArgumentException {
        super(name,type,startingValue);
        defaultValue = startingValue;
    }

    public void setFrom(Connection connection) {
        from = connection;
    }

    public Connection getFrom() {
        return from;
    }

    public boolean hasConnection() {
        return from!=null;
    }

    /**
     * @return an inverted copy of this {@link Port}.
     */
    @Override
    public @Nonnull Port<T> createInverse() {
        return new Output<T>(name,type,value);
    }

    public void setValueToDefault() {
        setValue(defaultValue);
    }
}
