package com.github.deroq1337.bansystem.web.data.exception.handlers;

import com.github.deroq1337.bansystem.web.data.exception.models.ExceptionResponse;
import com.github.deroq1337.bansystem.web.data.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceNotFoundExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(ResourceNotFoundException e) {
        return new ResponseEntity<>(
                new ExceptionResponse(404, e.getMessage()),
                HttpStatusCode.valueOf(404)
        );
    }
}
