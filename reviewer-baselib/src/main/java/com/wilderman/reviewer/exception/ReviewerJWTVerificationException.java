package com.wilderman.reviewer.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class ReviewerJWTVerificationException extends JWTVerificationException {

    public ReviewerJWTVerificationException(String message) {
        super(message);
    }

    public ReviewerJWTVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
