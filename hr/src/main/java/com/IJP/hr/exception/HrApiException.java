package com.IJP.hr.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HrApiException extends RuntimeException {

    private String message;
    private HttpStatus status;

    public HrApiException(String message, HttpStatus status) {
        this.message=message;
        this.status=status;
    }

    public HrApiException(String message, HttpStatus status,String customMessage) {
        super(customMessage);
        this.message=message;
        this.status=status;
    }
}
