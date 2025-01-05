package com.marginallyclever.nodegraphcore.dock;

import com.marginallyclever.nodegraphcore.Connection;

/**
 * An {@link Input} is a {@link Dock} that can receive data from a {@link Connection}.
 * @param <T> the type of data this {@link Input} receives.
 */
public class Input<T> extends Dock<T> {
    private Connection from;

    public Input(String name, Class<T> type, T startingValue) throws IllegalArgumentException {
        super(name,type,startingValue);
    }

    @Deprecated
    public void receive() {
        if(from==null) return;
        if(from.getOutput()==null) return;
        super.setValue(from.getOutput().getValue());
    }

    public void setFrom(Connection connection) {
        from = connection;
    }

    public void removeFrom(Connection connection) {
        if (from == connection) {
            from = null;
        }
    }

    public boolean hasConnection() {
        return from!=null;
    }

    /**
     * @return an inverted copy of this {@link Dock}.
     */
    @Override
    public Dock<T> createInverse() {
        return new Output<T>(name,type,value);
    }
}
