package com.marginallyclever.nodegraphcore.port;

import com.marginallyclever.nodegraphcore.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Output} is a {@link Port} that can send data to a {@link Connection}.
 * @param <T> the type of data this {@link Output} sends.
 */
public class Output<T> extends Port<T> {
    private final List<Connection> to = new ArrayList<>();

    public Output(String _name, Class<T> type, T startingValue) throws IllegalArgumentException {
        super(_name,type,startingValue);
    }

    public void send(T packet) {
        super.setValue(packet);
        for(Connection c : to) {
            var recipient = c.getInput();
            if(recipient!=null) recipient.setValue(packet);
        }
    }

    public void addTo(Connection connection) {
        to.add(connection);
    }

    public void removeTo(Connection connection) {
        to.remove(connection);
    }

    /**
     * @return an inverted copy of this {@link Port}.
     */
    @Override
    public Port<T> createInverse() {
        return new Input<>(name,type,value);
    }
}
