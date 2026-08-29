package com.ogoma.marketing.core.sharedkernel;

import java.io.Serializable;
import java.util.UUID;

public interface DomainEvent extends Serializable {

    default String type() {
        return this.getClass().getName();
    }

    String aggregateType();

    UUID aggregateID();

    String eventVersion();

    String eventName();

    default Serializable payload() {
        return this;
    }
}
