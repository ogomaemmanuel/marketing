package com.ogoma.marketing.core.domain.polls.exceptions;

public class InvalidPollOptionException extends RuntimeException{
    public InvalidPollOptionException(String message) {
        super(message);
    }
}
