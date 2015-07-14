package com.lge.lai.common.db;

public class LGAppInterfaceDBException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LGAppInterfaceDBException(String message) {
        super(message);
    }

    public LGAppInterfaceDBException(Throwable cause) {
        super(cause);
    }

    public LGAppInterfaceDBException(String message, Throwable cause) {
        super(message, cause);
    }
}
