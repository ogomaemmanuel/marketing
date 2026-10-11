package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;

import java.util.UUID;

public record ContactExportID(UUID id) implements TypedID<UUID> {
    public ContactExportID() {
        this(UUID.randomUUID());
    }
}
