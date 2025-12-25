package com.example.adoptapet.model.exceptions;

public class AlreadyAdoptedException extends Exception {
    public AlreadyAdoptedException(String message) {
        super(message);
    }
}