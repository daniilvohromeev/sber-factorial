package org.main.sberfactorial.exception;

import java.util.NoSuchElementException;

public class ResourceNotFoundException extends NoSuchElementException {
    public ResourceNotFoundException(String message, Long num){
        super(message + " " + num.toString());
    }
}
