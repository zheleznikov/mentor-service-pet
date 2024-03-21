package org.zheleznikov.authservice.exception;

public class SignupExceptionEmailAlreadyExists extends RuntimeException {

    public SignupExceptionEmailAlreadyExists( String message) {
        super(message);
    }
}
