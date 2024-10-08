package com.IJP.jobs.exceptions;

import com.IJP.jobs.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobsApiException.class)
    public ResponseEntity<ErrorDetails> handleHrApiException(JobsApiException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date().toString(), ex.getMessage(), request.getDescription(false
        ));
        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date().toString(), exception.getMessage(), request.getDescription(false
        ));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
