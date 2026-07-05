package com.ogoma.marketing.core.abstractions;

public interface MessageSenderService<M extends Message> {

    Class<M> supports();

    void send(M message);
}
