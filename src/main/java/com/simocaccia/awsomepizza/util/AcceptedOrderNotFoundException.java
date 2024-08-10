package com.simocaccia.awsomepizza.util;

public class AcceptedOrderNotFoundException extends RuntimeException {
    public AcceptedOrderNotFoundException(String message) {
        super(message);
    }
}
