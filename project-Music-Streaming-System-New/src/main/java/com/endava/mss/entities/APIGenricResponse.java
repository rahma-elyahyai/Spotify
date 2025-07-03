package com.endava.mss.entities;

import java.time.LocalDateTime;

import com.endava.mss.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIGenricResponse {

    private LocalDateTime timestamp;
    private String status;
    private String message;
    private Object body;
    private ErrorResponse errorResponse;

    public APIGenricResponse(String status, String message, Object body) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public APIGenricResponse(String status, String message, ErrorResponse errorResponse) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errorResponse = errorResponse;
    }
}
