package com.lge.lai.common.db.dao;

public class DAOConfigurationException extends RuntimeException {
    private static final long serialVersinUID = 1L;

    public DAOConfigurationException(String message) {
        super(message);
    }

    public DAOConfigurationException(Throwable cause) {
        super(cause);
    }

    public DAOConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
