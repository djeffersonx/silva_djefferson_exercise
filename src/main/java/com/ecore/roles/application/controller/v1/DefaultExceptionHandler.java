package com.ecore.roles.application.controller.v1;

import com.ecore.roles.exception.ErrorResponse;
import com.ecore.roles.exception.InvalidInputException;
import com.ecore.roles.exception.ResourceAlreadyExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        log.warn("Illegal argument: " + exception.getMessage(), exception);
        return createResponse(HttpStatus.BAD_REQUEST.value(), exception.getFieldError().getDefaultMessage());
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(HttpMessageNotReadableException exception) {
        log.warn("Message not readable: " + exception.getMessage(), exception);
        return createResponse(
                HttpStatus.BAD_REQUEST.value(),
                "The request input is required, please send a request body"
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(ResourceNotFoundException exception) {
        log.warn("Resource not found: " + exception.getMessage(), exception);
        return createResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(ResourceAlreadyExistsException exception) {
        log.warn("Resource already exists: " + exception.getMessage(), exception);
        return createResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(InvalidInputException exception) {
        log.warn("Invalid input error: " + exception.getMessage(), exception);
        return createResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(IllegalStateException exception) {
        log.error("Illegal state: " + exception.getMessage(), exception);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(Throwable exception) {
        log.error("Unknown error: " + exception.getMessage(), exception);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }

    private ResponseEntity<ErrorResponse> createResponse(int status, String exception) {
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .status(status)
                        .error(exception).build());
    }
}
