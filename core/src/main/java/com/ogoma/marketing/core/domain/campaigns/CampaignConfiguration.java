package com.ogoma.marketing.core.domain.campaigns;

import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;

import java.util.Set;

public record CampaignConfiguration(
//        @Transient
        Set<Channel> channels,


        EmailTemplateID emailTemplateID,

        SmsTemplateID smsTemplateID
) {
    public CampaignConfiguration {
        channels = channels == null ? Set.of() : Set.copyOf(channels);
    }
}
