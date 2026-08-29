package com.ogoma.marketing.core.domain.outbox;

import com.ogoma.marketing.core.sharedkernel.Entity;
import lombok.Getter;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Table("message_outbox")
@Getter
public class Outbox extends Entity<OutboxID> {
    private String aggregateType;
    @Version
    private Long version;
    private UUID aggregateID;
    private String eventType;
    private String eventVersion;
    private String eventName;
    private Payload payload;
    private Instant createdAt;
    private boolean processed;
    private Instant processedAt;

    private Outbox() {
        super(null);
    }

    private Outbox(
            String aggregateType,
            UUID aggregateID,
            String eventType,
            String eventVersion,
            String eventName,
            Serializable payload,
            Instant createdAt

    ) {
        Assert.notNull(payload, "Payload is required");
        Assert.notNull(aggregateID, "Aggregate id is required");
        Assert.hasText(aggregateType, "Aggregate type is required");
        Assert.hasText(eventType, "Event type  is required");
        Assert.hasText(eventVersion, "Event version  is required");
        Assert.hasText(eventName, "Event name is required");
        Assert.notNull(createdAt, "Created at is required");
        super(new OutboxID());
        this.aggregateType = aggregateType;
        this.aggregateID = aggregateID;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.eventName = eventName;
        this.payload = new Payload(payload);
        this.createdAt = createdAt;
    }

    public static Outbox createNew(
            String aggregateType,
            UUID aggregateID,
            String eventType,
            String eventVersion,
            String eventName,
            Serializable payload,
            Clock clock) {
        return new Outbox(aggregateType, aggregateID, eventType, eventVersion, eventName, payload, clock.instant());
    }

    public void markAsProcessed(Clock clock) {
        this.processed = true;
        this.processedAt = clock.instant();
    }
}
