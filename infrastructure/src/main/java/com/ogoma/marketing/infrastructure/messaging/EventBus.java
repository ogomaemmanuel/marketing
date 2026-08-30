package com.ogoma.marketing.infrastructure.messaging;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventBus {
    void publish(MessageEnvelope<?> message);

    CompletableFuture<Void> publishAsync(MessageEnvelope<?> message);

    void publishBatch(List<MessageEnvelope<?>> messages);
}
