package com.ogoma.marketing.infrastructure.composition;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "marketing.outbox")
@Configuration

public class OutboxProperties{
    Integer outboxMessageLimit=10000;

    public Integer getOutboxMessageLimit() {
        return outboxMessageLimit;
    }

    public void setOutboxMessageLimit(Integer outboxMessageLimit) {
        this.outboxMessageLimit = outboxMessageLimit;
    }
}


