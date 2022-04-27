package com.marginallyclever.nodegraphcore;

import java.util.ArrayList;
import java.util.List;

public class DockShipping<T> extends Dock<T> {
    private List<Connection> to = new ArrayList<>();

    public DockShipping(String _name, Class<T> type, T startingValue) throws IllegalArgumentException {
        super(_name,type,startingValue,false,true);
    }

    public void send(Packet<?> packet) {
        if(isValidType(packet.getData())) {
            super.setValue(packet.getData());
            for(Connection c : to) {
                c.send(packet);
            }
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
}
