package com.ogoma.marketing.core.domain.polls.exceptions;

public class PollNotPublishedException extends RuntimeException{
    public PollNotPublishedException(String message) {
        super(message);
    }
}
