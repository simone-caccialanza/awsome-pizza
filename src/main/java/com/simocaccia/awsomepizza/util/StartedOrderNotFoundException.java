package com.simocaccia.awsomepizza.util;

public class StartedOrderNotFoundException extends RuntimeException {
    public StartedOrderNotFoundException(String message) {
        super(message);
    }
}
