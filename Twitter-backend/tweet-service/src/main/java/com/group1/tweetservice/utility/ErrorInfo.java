package com.group1.tweetservice.utility;

import java.time.LocalDateTime;

public class ErrorInfo {
	
	private String errorCode;
	private String errorMessage;
	private LocalDateTime timestamp;
	
	public ErrorInfo() {
		this.timestamp = LocalDateTime.now();
	}
	
	public ErrorInfo(String errorCode , String errorMessage) {
		this.errorCode= errorCode;
		this.errorMessage= errorMessage;
		this.timestamp = LocalDateTime.now();
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode= errorCode;
		
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage= errorMessage;
		
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

}
