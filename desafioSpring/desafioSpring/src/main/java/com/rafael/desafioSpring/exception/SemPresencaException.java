package com.rafael.desafioSpring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SemPresencaException extends RuntimeException {

    /**
     *
     */
    
    private static final long serialVersionUID = -6754767023122935455L;

    public SemPresencaException(String message) {
        super(message);
    }

} 