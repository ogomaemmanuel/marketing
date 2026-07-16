package com.ogoma.marketing.core.sharedkernel;

import org.springframework.data.annotation.Transient;

import java.util.ArrayDeque;
import java.util.List;

public abstract class AggregateRoot<ID> extends Entity<ID> {
    @Transient
    private final ArrayDeque<DomainEvent> domainEvents = new ArrayDeque<>();

    protected AggregateRoot(ID id) {
        super(id);
    }

    protected final void raiseEvent(DomainEvent event) {
        CustomAssert.notNull(event, () -> new IllegalArgumentException("event required"));
        domainEvents.add(event);
    }

    public final List<DomainEvent> pullDomainEvents() {
        if (domainEvents.isEmpty()) {
            return List.of();
        }
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }
}
