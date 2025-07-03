package com.endava.mss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.endava.mss.entities.APIGenricResponse;

@Component
public class Exceptions {

	/**
	 * Generalized method to create error responses for various scenarios.
	 * 
	 * @param errorCode     The error code.
	 * @param errorMessage  The error message.
	 * @param errorDetails  Detailed error description.
	 * @param status        The HTTP status to return.
	 * @param returnMessage The message that will be returned to the user.
	 * @return ResponseEntity containing the error response.
	 */
	private ResponseEntity<APIGenricResponse> createErrorResponse(String errorCode, String errorMessage,
			String errorDetails, HttpStatus status, String returnMessage) {

		ErrorResponse errorResponse = new ErrorResponse(errorCode, errorMessage, errorDetails);

		APIGenricResponse response = new APIGenricResponse("Error", returnMessage, errorResponse);
 
		return new ResponseEntity<>(response, status);
	} 

	public ResponseEntity<APIGenricResponse> RecordNotFound(String errorCode, String errorMessage, String errorDetails,
			String returnMessage) {
		return createErrorResponse(errorCode, errorMessage, errorDetails, HttpStatus.NOT_FOUND, returnMessage);
	}

	public ResponseEntity<APIGenricResponse> EmptyField(String errorCode, String errorMessage, String errorDetails,
			String returnMessage) {
		return createErrorResponse(errorCode, errorMessage, errorDetails, HttpStatus.BAD_REQUEST, returnMessage);
	}

	public ResponseEntity<APIGenricResponse> EntityAlreadyExists(String errorCode, String errorMessage,
			String errorDetails, String returnMessage) {
		return createErrorResponse(errorCode, errorMessage, errorDetails, HttpStatus.BAD_REQUEST, returnMessage);
	}

	public ResponseEntity<APIGenricResponse> InvalidEmailDomain(String errorCode, String errorMessage,
			String errorDetails, String returnMessage) {
		return createErrorResponse(errorCode, errorMessage, errorDetails, HttpStatus.BAD_REQUEST, returnMessage);
	}

	public ResponseEntity<APIGenricResponse> InvalidCredentials(String errorCode, String errorMessage,
			String errorDetails, String returnMessage) {
		return createErrorResponse(errorCode, errorMessage, errorDetails, HttpStatus.UNAUTHORIZED, returnMessage);
	}

	public ResponseEntity<APIGenricResponse> InvalidEmail(String errorCode, String errorMessage, String errorDetails,
			String returnMessage) {
		return createErrorResponse(errorCode, errorMessage, errorDetails, HttpStatus.BAD_REQUEST, returnMessage);
	}
}


