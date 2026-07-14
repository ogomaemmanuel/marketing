package com.ogoma.marketing.core.application.contacts.commands;

public class InvalidAudienceException extends RuntimeException{
    public InvalidAudienceException(String msg){
        super(msg);
    }
}
