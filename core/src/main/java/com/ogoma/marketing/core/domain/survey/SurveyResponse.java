package com.ogoma.marketing.core.domain.survey;

import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;

import java.util.List;

public class SurveyResponse extends AggregateRoot<SurveyResponseID> {
    private SurveyResponseStatus status;
    private List<SurveyAnswer> answers;
    protected SurveyResponse() {
        super(null);
    }
}
