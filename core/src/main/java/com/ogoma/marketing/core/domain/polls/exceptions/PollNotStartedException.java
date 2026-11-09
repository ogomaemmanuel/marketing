package com.ogoma.marketing.core.domain.polls.entities;

public class PollNotStartedException extends RuntimeException{
    public PollNotStartedException(String message) {
        super(message);
    }
}
