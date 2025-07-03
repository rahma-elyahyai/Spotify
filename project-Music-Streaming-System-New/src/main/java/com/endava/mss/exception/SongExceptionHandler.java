package com.endava.mss.exception;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.endava.mss.entities.APIGenricResponse;

@RestControllerAdvice
public class SongExceptionHandler {

	@ExceptionHandler(SongNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleSongNotFoundException(SongNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse("SONG_NOT_FOUND", ex.getMessage(), "Song is not found.");
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<APIGenricResponse> handleMissingPart(MissingServletRequestPartException ex) {

		String missingPart = ex.getRequestPartName();

		ErrorResponse errorResponse = new ErrorResponse("MISSING_PART",
				"Required part '" + missingPart + "' is missing.",
				"Please ensure all required file parts are included in the request.");
		
		APIGenricResponse response = new APIGenricResponse("Error","Missing part", errorResponse);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<APIGenricResponse> handleReleaseDatePart(MissingServletRequestParameterException ex) {


		ErrorResponse errorResponse = new ErrorResponse("MISSING_PART",
				"Required part " + "Release date" + " is missing.",
				"Please ensure all required file parts are included in the request.");

		APIGenricResponse response = new APIGenricResponse("Error","Missing part", errorResponse);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public ResponseEntity<APIGenricResponse> handleAlumMissingPart(InvalidDataAccessApiUsageException ex) {


		ErrorResponse errorResponse = new ErrorResponse("MISSING_PART",
				"Required part " + "album" + " is missing.",
				"Please ensure all required file parts are included in the request.");

		APIGenricResponse response = new APIGenricResponse("Error","Missing part", errorResponse);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
