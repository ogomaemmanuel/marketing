package com.ogoma.marketing.core.domain.survey;

import com.ogoma.marketing.core.domain.survey.events.SurveyClosedEvent;
import com.ogoma.marketing.core.domain.survey.events.SurveyPublishedEvent;
import com.ogoma.marketing.core.domain.survey.valueobjects.Question;
import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Table(name = "surveys")
@Getter
public class Survey extends AggregateRoot<SurveyID> {
    private String name;
    private SurveyStatus status;
    private SurveySetting setting;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    private int surveyVersion;
    private final List<Question> questions = new ArrayList<>();

    private Survey() {
        super(null);
    }

    private Survey(String name, SurveySetting setting, List<Question> questions, Instant createdAt) {
        super(new SurveyID());
        this.name = name;
        this.setting = setting;
        this.questions.addAll(questions);
        this.status = SurveyStatus.DRAFT;
        this.createdAt = createdAt;
        this.surveyVersion = 0;

    }


    public static Survey createNew(
            String name, SurveySetting setting, List<Question> questions, Clock clock
    ) {
        Assert.hasText(name, "Name is required");
        Assert.notEmpty(questions, "Questions cannot be empty");
        Assert.notNull(clock, "Clock is required");
        Assert.noNullElements(questions, "Questions cannot have a null");
        return new Survey(
                name, setting, questions, clock.instant()
        );
    }

    /**
     * Updates survey details and increments the domain version.
     */
    public void updateDetails(String name, SurveySetting setting, List<Question> newQuestions, Clock clock) {
        Assert.hasText(name, "Name is required");
        Assert.notEmpty(newQuestions, "Questions cannot be empty");
        Assert.notNull(clock, "Clock is required");
        Assert.noNullElements(newQuestions, "Questions cannot have a null");
        if (this.status == SurveyStatus.CLOSED) {
            throw new IllegalStateException("Cannot update a closed survey");
        }
        this.name = name;
        this.setting = setting;
        this.questions.clear();
        this.questions.addAll(newQuestions);
        // Version tracking increments on structural modification
        this.lastUpdatedAt = clock.instant();
        this.surveyVersion++;
    }


    public void publish(Clock clock) {
        if (this.status != SurveyStatus.DRAFT) {
            throw new IllegalStateException("Only surveys in draft status can be published");
        }
        if (this.questions.isEmpty()) {
            throw new IllegalStateException("Cannot publish a survey with no questions");
        }
        this.status = SurveyStatus.ACTIVE;
        this.lastUpdatedAt = clock.instant();
        this.raiseEvent(new SurveyPublishedEvent(this.getId().id()));
    }

    public void close(Clock clock) {
        if (this.status == SurveyStatus.CLOSED) {
            throw new IllegalStateException("Survey is already closed");
        }
        this.status = SurveyStatus.CLOSED;
        this.lastUpdatedAt = clock.instant();
        this.raiseEvent(new SurveyClosedEvent(this.getId().id()));
    }

}
