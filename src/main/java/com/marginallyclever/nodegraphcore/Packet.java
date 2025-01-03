package com.marginallyclever.nodegraphcore;

public class Packet<T> {
    private T data;

    public Packet(T data) {
        this.data=data;
    }

    public T getData() {
        return data;
    }
}
