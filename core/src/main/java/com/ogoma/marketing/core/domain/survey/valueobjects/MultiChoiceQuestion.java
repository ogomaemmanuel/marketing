package com.ogoma.marketing.core.domain.survey.valueobjects;

import java.util.List;
import java.util.UUID;

public record MultiChoiceQuestion(
        List<QuestionOption> options,
        boolean isRequired,
        String text,
        UUID id
) implements Question {

}
