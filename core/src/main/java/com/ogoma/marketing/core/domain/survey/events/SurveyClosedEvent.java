package com.ogoma.marketing.core.domain.survey.events;

import com.ogoma.marketing.core.domain.survey.Survey;
import com.ogoma.marketing.core.sharedkernel.ddd.DomainEvent;

import java.util.UUID;

public  record SurveyClosedEvent(
        UUID aggregateID
) implements DomainEvent {
    @Override
    public String aggregateType() {
        return Survey.class.getName();
    }


    @Override
    public String eventVersion() {
        return "V0";
    }

    @Override
    public String eventName() {
        return "survey-closed-event";
    }
}
