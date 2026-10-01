package com.ogoma.marketing.infrastructure.workflows.abstractions;

import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import com.ogoma.marketing.infrastructure.workflows.dto.CampaignDetails;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.Optional;

@ActivityInterface
public interface SendCampaignActivities {



    @ActivityMethod
    Optional<CampaignDetails> getCampaignDetails(CampaignID campaignID);

    @ActivityMethod
    void sendEmail(CampaignID campaignID, ContactID contactID, EmailTemplateID emailTemplateID);

    @ActivityMethod
    void sendSms(CampaignID campaignID, ContactID contactID, SmsTemplateID smsTemplateID);
}
