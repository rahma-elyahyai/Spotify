package com.endava.mss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SongQueueHandler {

    @ExceptionHandler(EmptyQueueException.class)
    public ResponseEntity<ErrorResponse> handleEmptyQueueException(EmptyQueueException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "EMPTY_QUEUE", 
                ex.getMessage(),        
                "Queue is empty." 
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); 
    }
    
    @ExceptionHandler(SongQueueNotFoundException.class)
    public ResponseEntity<ErrorResponse> SongQueueNotException(SongQueueNotFoundException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "NOT_FOUND", 
                ex.getMessage(),        
                "Queue is not found." 
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); 
    }
}
