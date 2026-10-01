package com.ogoma.marketing.core.abstractions;

import com.ogoma.marketing.core.sharedkernel.ddd.DomainEvent;

public interface EventRouter {

    void route(DomainEvent event);
}
