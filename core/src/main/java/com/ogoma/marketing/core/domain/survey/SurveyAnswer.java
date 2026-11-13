package com.ogoma.marketing.core.domain.survey;

import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;


@Table(name = "survey")
@Getter
public class SurveyAnswer {
    private UUID questionId;
    private String answer;

    public SurveyAnswer(
            UUID questionId,
            String answer
    ) {
        this.questionId = questionId;
        this.answer = answer;
    }
}
