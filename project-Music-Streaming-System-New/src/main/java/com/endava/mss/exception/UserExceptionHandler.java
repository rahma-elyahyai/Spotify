package com.endava.mss.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
	    ErrorResponse errorResponse = new ErrorResponse(
	            "INTERNAL_SERVER_ERROR", 
	            "An unexpected error occurred", 
	            ex.getMessage()
	    );
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(errorResponse);
	}
	

	@ExceptionHandler(InvalidLengthException.class)
	public ResponseEntity<ErrorResponse> handleInvalidException(InvalidLengthException ex) {
	    ErrorResponse errorResponse = new ErrorResponse(
	            "INVALID_LENGTH", 
	            "Input length is invalid. Please make sure the input meets the required length", 
	            ex.getMessage()
	    );
	    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "EMAIL_ALREADY_EXISTS", 
                ex.getMessage(),        
                "Please make sure the email address is not already registered." 
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); 
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "USER_NOT_FOUND", 
                ex.getMessage(),        
                "Please make sure that user has signed up." 
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); 
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> InvalidCredentialsException(InvalidCredentialsException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_CREDENTIALS", 
                ex.getMessage(),        
                "Please make sure that the credentials are correct." 
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED); 
    }
    
    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<ErrorResponse> handleEmptyFieldException(EmptyFieldException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "EMPTY_FIELDS", 
                ex.getMessage(),        
                "Please provide the required fields." 
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); 
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "A database constraint was violated.";
        
        if (ex.getCause() != null && ex.getCause().getMessage().contains("UK")) {
            errorMessage = "This email is already registered. Please use a different email.";
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "DATABASE_ERROR",
                errorMessage,
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
