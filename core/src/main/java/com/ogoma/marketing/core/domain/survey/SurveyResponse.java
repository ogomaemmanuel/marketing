package com.ogoma.marketing.core.domain.survey;

import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;

import java.util.List;

public class SurveyResponse extends AggregateRoot<SurveyResponseID> {

    private SurveyID surveyID;

    private Integer surveyVersion;
    private SurveyResponseStatus status;
    private List<SurveyAnswer> answers;
    private SurveyResponse() {
        super(null);
    }
}
