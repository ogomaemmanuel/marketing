package com.ogoma.marketing.core.domain.survey.valueobjects;

import java.util.UUID;

public record TextQuestion(
        UUID id,
      boolean isRequired,
      String text

) implements Question{
}
