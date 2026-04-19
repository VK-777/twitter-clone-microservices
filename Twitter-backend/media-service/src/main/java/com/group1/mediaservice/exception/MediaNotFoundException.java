package com.group1.mediaservice.exception;

public class MediaNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	public MediaNotFoundException(String message) {
		super(message);
	}
	public MediaNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
