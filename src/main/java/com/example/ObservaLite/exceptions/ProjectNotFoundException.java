package com.example.ObservaLite.exceptions;

import lombok.Getter;

@Getter
public class ProjectNotFoundException extends RuntimeException {
    private final int status;

    public ProjectNotFoundException(int status, String message) {
        super(message);
        this.status = status;
    }

}
