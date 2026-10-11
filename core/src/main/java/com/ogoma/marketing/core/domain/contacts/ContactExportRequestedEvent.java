package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.sharedkernel.ddd.DomainEvent;

import java.util.UUID;

public record ContactExportRequestedEvent(
        UUID aggregateID
) implements DomainEvent {
    @Override
    public String aggregateType() {
        return ContactExportJob.class.getName();
    }

    @Override
    public String eventVersion() {
        return "V0";
    }

    @Override
    public String eventName() {
        return "contact-export-requested-event";
    }
}
