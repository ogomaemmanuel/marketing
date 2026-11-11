package com.ogoma.marketing.core.domain.polls.exceptions;

public class PollClosedException extends RuntimeException{
    public PollClosedException(String message) {
        super(message);
    }
}
