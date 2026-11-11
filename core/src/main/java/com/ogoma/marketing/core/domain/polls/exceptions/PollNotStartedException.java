package com.ogoma.marketing.core.domain.polls.exceptions;

public class PollNotStartedException extends RuntimeException{
    public PollNotStartedException(String message) {
        super(message);
    }
}
