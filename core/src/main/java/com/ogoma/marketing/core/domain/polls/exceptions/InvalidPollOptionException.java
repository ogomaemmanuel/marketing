package com.ogoma.marketing.core.domain.polls.entities;

public class InvalidPollOptionException extends RuntimeException{
    public InvalidPollOptionException(String message) {
        super(message);
    }
}
