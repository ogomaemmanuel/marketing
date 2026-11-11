package com.ogoma.marketing.core.domain.survey;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;

import java.util.UUID;

public record SurveyVersionID(
        UUID id
) implements TypedID<UUID> {
}
