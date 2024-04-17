package org.main.sberfactorial.exception;

public class CacheInvalidException extends IllegalStateException{
    public CacheInvalidException(String message) {
        super(message);
    }
}