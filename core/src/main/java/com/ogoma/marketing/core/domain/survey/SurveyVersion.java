package com.ogoma.marketing.core.domain.survey;

import com.ogoma.marketing.core.domain.survey.valueobjects.Question;
import com.ogoma.marketing.core.sharedkernel.ddd.Entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SurveyVersion extends Entity<SurveyVersionID> {
    private SurveyID surveyID;

    private String name;
    private SurveySetting setting;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    private int surveyVersion;
    private List<Question> questions = new ArrayList<>();

    protected SurveyVersion() {
        super(null);
    }
}
