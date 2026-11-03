package com.ogoma.marketing.infrastructure.workflows.implementations;

import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.Channel;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
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
import java.util.UUID;

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
                for (Channel channel : campaignEntity.channels()) {

                    // Explicitly split by channel type for independent execution paths
                    if (channel == Channel.SMS && campaignEntity.smsTemplateID() != null) {
                        promises.add(Async.procedure(() -> sendSmsWorkflowTask(startRequest.campaignID(), contactID, campaignEntity.smsTemplateID())));
                    } else if (channel == Channel.EMAIL && campaignEntity.emailTemplateID() != null) {
                        promises.add(Async.procedure(() -> sendEmailWorkflowTask(startRequest.campaignID(), contactID, campaignEntity.emailTemplateID())));
                    }
                }
            }
            Promise.allOf(promises).get();
        });

    }

    private void sendSmsWorkflowTask(CampaignID campaignID, ContactID contactID, SmsTemplateID templateID) {
        UUID trackingId = Workflow.randomUUID();
        try {
            activity.markInProgress(campaignID, contactID, Channel.SMS, trackingId);
            activity.sendSms(campaignID, contactID, templateID, trackingId);
            activity.markSent(campaignID, contactID, Channel.SMS);
        } catch (Exception e) {
            activity.markFailed(campaignID, contactID, Channel.SMS, e.getMessage() != null ? e.getMessage() : e.getClass().getName());
        }
    }

    private void sendEmailWorkflowTask(CampaignID campaignID, ContactID contactID, EmailTemplateID templateID) {
        UUID trackingId = Workflow.randomUUID();
        try {
            activity.markInProgress(campaignID, contactID, Channel.EMAIL, trackingId);
            // Using the email-specific activity stub if configured differently
            activity.sendEmail(campaignID, contactID, templateID, trackingId);
            activity.markSent(campaignID, contactID, Channel.EMAIL);
        } catch (Exception e) {
            activity.markFailed(campaignID, contactID, Channel.EMAIL, e.getMessage() != null ? e.getMessage() : e.getClass().getName());
        }
    }
}
