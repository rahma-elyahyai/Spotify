package com.endava.mss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LiveConcertExceptionHandler {

    @ExceptionHandler(LiveConcertNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLiveConcertNotFoundException(LiveConcertNotFoundException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "LIVECONCERT_NOT_FOUND", 
                ex.getMessage(),        
                "LiveConcert is not found." 
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); 
    }
}
