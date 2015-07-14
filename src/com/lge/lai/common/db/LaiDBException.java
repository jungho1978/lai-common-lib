package com.lge.lai.common.db;

public class LaiDBException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public LaiDBException(String message) {
		super(message);
	}
	
	public LaiDBException(Throwable cause) {
		super(cause);
	}
	
	public LaiDBException(String message, Throwable cause) {
		super(message, cause);
	}
}
