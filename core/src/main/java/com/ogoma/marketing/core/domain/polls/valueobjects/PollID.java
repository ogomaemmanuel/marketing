package com.ogoma.marketing.core.domain.polls.valueobjects;

import com.ogoma.marketing.core.sharedkernel.UUIDV7Generator;
import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;

import java.util.UUID;

public record PollID(UUID id) implements TypedID<UUID> {

    public PollID(){
        this(UUIDV7Generator.generate());
    }
}
