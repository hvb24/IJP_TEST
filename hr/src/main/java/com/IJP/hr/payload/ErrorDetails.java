package com.IJP.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ErrorDetails {

    private String timestamp;
    private String message;
    private String details;
}
