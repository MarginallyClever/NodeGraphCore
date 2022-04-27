package com.marginallyclever.nodegraphcore;

public class Packet<T> {
    private T data;

    public Packet(T data) {
        this.data=data;
    }

    T getData() {
        return data;
    }
}
