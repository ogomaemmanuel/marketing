package com.ogoma.marketing.infrastructure.workflows.dto;

import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.Channel;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;

import java.util.Set;

public record CampaignDetails(
        CampaignID id,
        Set<Channel> channels,
        SmsTemplateID smsTemplateID,
        EmailTemplateID emailTemplateID
) {

}
