package com.ogoma.marketing.core.domain.polls.entities;

public class PollClosedException extends RuntimeException{
    public PollClosedException(String message) {
        super(message);
    }
}
