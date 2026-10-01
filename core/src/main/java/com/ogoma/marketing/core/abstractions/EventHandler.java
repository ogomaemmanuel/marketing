package com.ogoma.marketing.core.abstractions;

import com.ogoma.marketing.core.sharedkernel.ddd.DomainEvent;

public interface EventHandler<E extends DomainEvent> {
    Class<E> supports();
    void handle(E event);
}
