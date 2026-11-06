package com.ogoma.marketing.core.domain.polls.entities;

import com.ogoma.marketing.core.sharedkernel.UUIDV7Generator;
import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.springframework.util.Assert;

import java.util.UUID;

public record PollVoteID(UUID id) implements TypedID<UUID> {
    public PollVoteID{
        Assert.notNull(id,"%s id is required".formatted(this.getClass().getSimpleName()));
    }
    public PollVoteID(){
        this(UUIDV7Generator.generate());
    }
}
