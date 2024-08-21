package com.IJP.candidate.exception;

import com.IJP.candidate.entity.Candidate;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CandidateApiException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public CandidateApiException(String message,HttpStatus status){
        this.status=status;
        this.message=message;
    }

    public CandidateApiException(String message, HttpStatus status,String customMessage) {
        super(customMessage);
        this.message=message;
        this.status=status;
    }


}
