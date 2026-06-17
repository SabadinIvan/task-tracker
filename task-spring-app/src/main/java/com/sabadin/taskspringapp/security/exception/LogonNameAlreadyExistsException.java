package com.sabadin.taskspringapp.security.exception;

public class LogonNameAlreadyExistsException extends RuntimeException {
    public LogonNameAlreadyExistsException(String message) {
        super(message);
    }
}