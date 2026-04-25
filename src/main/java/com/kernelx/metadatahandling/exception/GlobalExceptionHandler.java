package com.kernelx.metadatahandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // This method handles all RuntimeExceptions thrown by your services
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {

        // 1. This shows the error in RED in your IDE Terminal
        log.error("TERMINAL LOG [ERROR]: {}", ex.getMessage());

        // 2. This sends a 404 NOT FOUND status to Postman
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}