package com.ogoma.marketing.core.domain.survey;

import com.ogoma.marketing.core.sharedkernel.UUIDV7Generator;
import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.springframework.util.Assert;

import java.util.UUID;

public record SurveyID(UUID id) implements TypedID<UUID> {
    public SurveyID{
        Assert.notNull(id,"%s id is required".formatted(this.getClass().getSimpleName()));
    }
    public SurveyID(){
        this(UUIDV7Generator.generate());
    }
}
