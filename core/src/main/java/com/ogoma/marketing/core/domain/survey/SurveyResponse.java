package com.ogoma.marketing.core.domain.survey;

import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;


@Getter
@Table(name = "survey_responses")
public class SurveyResponse extends AggregateRoot<SurveyResponseID> {

    private SurveyID surveyID;

    private Integer surveyVersion;
    private SurveyResponseStatus status;
    private List<SurveyAnswer> answers;

    private SurveyResponse() {
        super(null);
    }

    private SurveyResponse(
            SurveyID surveyID,
            Integer surveyVersion,
            SurveyResponseStatus status,
            List<SurveyAnswer> answers
    ) {
        super(new SurveyResponseID());
        this.surveyID = surveyID;
        this.surveyVersion = surveyVersion;
        this.status = status;
        this.answers = answers;
    }

    public static SurveyResponse createNew(
            SurveyID surveyID,
            Integer surveyVersion,
            SurveyResponseStatus status,
            List<SurveyAnswer> answers
    ) {
        return new SurveyResponse(surveyID, surveyVersion, status, answers);
    }
}
