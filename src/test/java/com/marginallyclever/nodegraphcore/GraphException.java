package com.marginallyclever.nodegraphcore;

public class GraphException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GraphException(String message) {
        super(message);
    }

    public GraphException(String message, Throwable cause) {
        super(message, cause);
    }
}
