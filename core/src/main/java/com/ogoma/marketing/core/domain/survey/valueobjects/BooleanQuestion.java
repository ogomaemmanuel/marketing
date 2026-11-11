package com.ogoma.marketing.core.domain.survey.valueobjects;

import java.util.UUID;

public record BooleanQuestion(
        boolean isRequired,
        String text,
        UUID id
) implements Question {
}
