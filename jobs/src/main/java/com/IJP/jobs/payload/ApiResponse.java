package com.IJP.jobs.payload;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {
    private T data;
    private String msg;
    private HttpStatus status;

    // Constructors, getters, and setters

    public ApiResponse(T data, String msg, HttpStatus status) {
        this.data = data;
        this.msg = msg;
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
