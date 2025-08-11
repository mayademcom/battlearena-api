package com.mayadem.battlearena.api.exception;

public class DisplayNameNotUniqueException extends RuntimeException {
    public DisplayNameNotUniqueException(String message) {
        super(message);
    }
}