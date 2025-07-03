package com.endava.mss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PlayListExceptionHandler {

    @ExceptionHandler(PlayListNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlayListNotFoundException(PlayListNotFoundException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "PLAYLIST_NOT_FOUND", 
                ex.getMessage(),        
                "PlayList is not found." 
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); 
    }
    
    @ExceptionHandler(SongAlreadyExistsInPlayListException.class)
    public ResponseEntity<ErrorResponse> handleSongAlreadyExistsInPlayListException(SongAlreadyExistsInPlayListException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "SONG_ALREADY_EXISTS", 
                ex.getMessage(),        
                "Song already exists in playlist." 
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); 
    }
}
