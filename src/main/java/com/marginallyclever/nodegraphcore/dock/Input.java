package com.marginallyclever.nodegraphcore.dock;

import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.Packet;

/**
 * An {@link Input} is a {@link Dock} that can receive a {@link Packet} from a {@link Connection}.
 * @param <T> the type of data this {@link Input} receives.
 */
public class Input<T> extends Dock<T> {
    private Connection from;

    public Input(String name, Class<T> type, T startingValue) throws IllegalArgumentException {
        super(name,type,startingValue);
    }

    public void receive() {
        if(from==null) return;

        Packet<?> packet = from.get();
        if(packet==null) return;
        super.setValue(packet.getData());
    }

    public boolean hasPacketWaiting() {
        return from!=null && !from.isEmpty();
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
