package com.ogoma.marketing.core.domain.survey.valueobjects;

import java.util.List;
import java.util.UUID;

public record SurveyScreen(
        UUID id,
        String title,

        Integer indexPosition,
        List<Question> questions
) {

}
