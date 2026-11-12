package com.ogoma.marketing.core.domain.survey;

import com.ogoma.marketing.core.sharedkernel.UUIDV7Generator;
import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;

import java.util.UUID;

public record SurveyResponseID(UUID id) implements TypedID<UUID> {

    public SurveyResponseID(){
        this(UUIDV7Generator.generate());
    }
}
