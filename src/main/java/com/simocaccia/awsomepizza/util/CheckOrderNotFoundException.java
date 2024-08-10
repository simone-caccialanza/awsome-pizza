package com.simocaccia.awsomepizza.util;

public class CheckOrderNotFoundException extends RuntimeException {
    public CheckOrderNotFoundException(String message) {
        super(message);
    }
}
