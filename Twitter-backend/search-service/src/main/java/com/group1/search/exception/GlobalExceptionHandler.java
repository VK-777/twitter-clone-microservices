package com.group1.search.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

   // Handle custom exceptions
   @ExceptionHandler(CustomException.class)
   public ResponseEntity<String> handleCustomException(CustomException ex) {
       return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
   }

   // Handle general exceptions
   @ExceptionHandler(Exception.class)
   public ResponseEntity<String> handleGeneralException(Exception ex) {
       return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
   }
}