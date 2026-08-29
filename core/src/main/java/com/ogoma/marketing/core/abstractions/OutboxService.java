package com.ogoma.marketing.core.abstractions;

public interface OutboxService {
    void processPendingMessages();
}
