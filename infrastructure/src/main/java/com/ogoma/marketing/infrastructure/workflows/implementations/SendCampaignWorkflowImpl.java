package com.ogoma.marketing.infrastructure.workflows.implementations;

import com.ogoma.marketing.core.domain.campaigns.Channel;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.infrastructure.workflows.abstractions.SendCampaignActivities;
import com.ogoma.marketing.infrastructure.workflows.abstractions.SendCampaignWorkflow;
import com.ogoma.marketing.infrastructure.workflows.abstractions.StartSendCampaignWorkflowCommand;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SendCampaignWorkflowImpl implements SendCampaignWorkflow {
    ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(60))
            .build();
    private final SendCampaignActivities activity = Workflow.newActivityStub(SendCampaignActivities.class, options);

    @Override
    public void start(StartSendCampaignWorkflowCommand startRequest) {

        activity.getCampaignDetails(startRequest.campaignID()).ifPresent(campaignEntity -> {
            List<Promise<Void>> promises = new ArrayList<>();

            for (ContactID contactID : startRequest.contacts()
            ) {
                campaignEntity.channels().forEach(channel -> {
                    if (channel == Channel.SMS) {
                        promises.add(Async.procedure(activity::sendSms,
                                startRequest.campaignID(), contactID, campaignEntity.smsTemplateID()));
                    }
                    if (channel == Channel.EMAIL) {
                        promises.add(
                                Async.procedure(activity::sendEmail, startRequest.campaignID(), contactID, campaignEntity.emailTemplateID()));
                    }
                });
            }
            Promise.allOf(promises).get();
        });

    }
}
