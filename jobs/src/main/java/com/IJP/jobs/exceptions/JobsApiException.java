package com.IJP.jobs.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class JobsApiException extends RuntimeException{
    private String message;
    private HttpStatus status;

    public JobsApiException(String message, HttpStatus status) {
        this.message=message;
        this.status=status;
    }

    public JobsApiException (String message, HttpStatus status,String customMessage) {
        super(customMessage);
        this.message=message;
        this.status=status;
    }
}
