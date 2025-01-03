package com.marginallyclever.nodegraphcore.dock;

import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Packet;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Output} is a {@link Dock} that can send a {@link Packet} to a {@link Connection}.
 * @param <T> the type of data this {@link Output} sends.
 */
public class Output<T> extends Dock<T> {
    private List<Connection> to = new ArrayList<>();

    public Output(String _name, Class<T> type, T startingValue) throws IllegalArgumentException {
        super(_name,type,startingValue);
    }

    public void send(Packet<?> packet) {
        super.setValue(packet.getData());
        for(Connection c : to) {
            c.send(packet);
        }
    }

    public void addTo(Connection connection) {
        to.add(connection);
    }

    public void removeTo(Connection connection) {
        to.remove(connection);
    }

    public boolean outputHasRoom() {
        for(Connection c : to) {
            if(!c.isEmpty()) return false;
        }
        return true;
    }

    /**
     * @return an inverted copy of this {@link Dock}.
     */
    @Override
    public Dock<T> createInverse() {
        return new Input<>(name,type,value);
    }
}
