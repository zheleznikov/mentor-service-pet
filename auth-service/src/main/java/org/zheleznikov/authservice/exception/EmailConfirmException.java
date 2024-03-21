package org.zheleznikov.authservice.exception;

public class EmailConfirmException extends RuntimeException {
    public EmailConfirmException(String message) {
        super(message);
    }
}
