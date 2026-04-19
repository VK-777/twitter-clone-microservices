package com.group1.mediaservice.utility;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.group1.mediaservice.exception.MediaNotFoundException;

@ControllerAdvice
public class ExceptionControllerAdvice {
	
	@ExceptionHandler(MediaNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleMediaNotFoundException(MediaNotFoundException ex, WebRequest request){
		ErrorInfo errorInfo = new ErrorInfo(LocalDateTime.now(),
				ex.getMessage(),
				request.getDescription(false)
				);
		
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorInfo> handleBadRequestException(IllegalArgumentException ex, WebRequest request){
		ErrorInfo errorInfo = new ErrorInfo(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> handleGlobalException(Exception ex, WebRequest request){
		ErrorInfo errorInfo = new ErrorInfo(LocalDateTime.now(), ex.getMessage(), "Please contact support check logs");
		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
