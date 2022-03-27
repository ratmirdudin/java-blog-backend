package com.ratmirdudin.jblog_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String property, String value) {
        super(resource + " not found with " + property + ": " + value);
    }

    public ResourceNotFoundException(String resource, String property, Long value) {
        this(resource, property, value.toString());
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
        super("Resource not found");
    }
}
