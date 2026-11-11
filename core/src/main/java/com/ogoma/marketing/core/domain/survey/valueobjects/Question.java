package com.ogoma.marketing.core.domain.survey.valueobjects;

import java.util.UUID;

public sealed interface Question permits BooleanQuestion, DateQuestion, EmailQuestion, MultiChoiceQuestion, RatingQuestion, SingleChoiceQuestion, TextQuestion {
     boolean isRequired();

     UUID id();
     String text();
}
