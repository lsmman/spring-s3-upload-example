package com.demo.demos3.config.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ClientException.class)
    public String clientException(ClientException e) {
        System.err.println(e.getCause().getMessage());
        return e.getMessage();
    }

}
