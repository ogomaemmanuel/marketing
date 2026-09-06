package com.ogoma.marketing.core.abstractions;

import com.ogoma.marketing.core.sharedkernel.ddd.DomainEvent;

public interface EventHandler<E extends DomainEvent> {
    void handle(E event);
}
