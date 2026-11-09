package com.ogoma.marketing.core.domain.polls.entities;

public class PollNotPublishedException extends RuntimeException{
    public PollNotPublishedException(String message) {
        super(message);
    }
}
