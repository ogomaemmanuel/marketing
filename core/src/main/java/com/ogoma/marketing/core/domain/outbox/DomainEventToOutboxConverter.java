package com.ogoma.marketing.core.domain.outbox;

import com.ogoma.marketing.core.sharedkernel.ddd.DomainEvent;

import java.time.Clock;

public class DomainEventToOutboxConverter {

    public static  Outbox convert(DomainEvent domainEvent, Clock clock){
        return Outbox.createNew(
                domainEvent.aggregateType(),
                domainEvent.aggregateID(),
                domainEvent.type(),
                domainEvent.eventVersion(),
                domainEvent.eventName(),
                domainEvent.payload(),
                clock
        );
    }
}
