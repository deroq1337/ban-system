package com.github.deroq1337.bansystem.web.data.exception.handlers;

import com.github.deroq1337.bansystem.web.data.exception.BanSystemUnavailableException;
import com.github.deroq1337.bansystem.web.data.exception.ExceptionResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BanSystemUnavailableExceptionHandler {

    @ExceptionHandler(BanSystemUnavailableException.class)
    public ResponseEntity<ExceptionResponse> handleException(BanSystemUnavailableException e) {
        return new ResponseEntity<>(
                new ExceptionResponse(503, e.getMessage()),
                HttpStatusCode.valueOf(503)
        );
    }
}