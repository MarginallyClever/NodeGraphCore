package com.marginallyclever.nodegraphcore.dock;

import com.marginallyclever.nodegraphcore.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Output} is a {@link Dock} that can send data to a {@link Connection}.
 * @param <T> the type of data this {@link Output} sends.
 */
public class Output<T> extends Dock<T> {
    private final List<Connection> to = new ArrayList<>();

    public Output(String _name, Class<T> type, T startingValue) throws IllegalArgumentException {
        super(_name,type,startingValue);
    }

    public void send(T packet) {
        super.setValue(packet);
        for(Connection c : to) {
            var to = c.getInput();
            if(to!=null) to.setValue(packet);
        }
    }

    public void addTo(Connection connection) {
        to.add(connection);
    }

    public void removeTo(Connection connection) {
        to.remove(connection);
    }

    /**
     * @return an inverted copy of this {@link Dock}.
     */
    @Override
    public Dock<T> createInverse() {
        return new Input<>(name,type,value);
    }
}
