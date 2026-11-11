package com.ogoma.marketing.core.domain.survey.valueobjects;

import com.ogoma.marketing.core.sharedkernel.ddd.Entity;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;


@Table(name = "survey_questions")
@Getter
public class Question extends Entity<UUID> {
    private String text;
    private QuestionType type;

    private boolean required;

    private Question() {
        super(null);
    }

    public Question(
            String text,
            QuestionType type,
            boolean required

    ) {
        super(UUID.randomUUID());
        this.text = text;
        this.required = required;
        this.type = type;
    }
}
