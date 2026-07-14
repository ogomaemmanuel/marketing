package com.ogoma.marketing.core.domain.contacts;

import java.util.UUID;

public record AudienceMembershipID(UUID id) {
    AudienceMembershipID(){
        this(UUID.randomUUID());
    }
}
