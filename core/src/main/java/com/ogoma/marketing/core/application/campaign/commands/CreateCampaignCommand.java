package com.ogoma.marketing.core.application.campaign.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.Channel;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;

import java.util.Set;

public record CreateCampaignCommand(
        String name,
        String description,
        Set<Channel> channels,
        Set<AudienceId> targetAudienceIds,

        EmailTemplateID emailTemplateID,

        SmsTemplateID smsTemplateID,

        String userId


) implements Command<CampaignID> {
    public CreateCampaignCommand {
        channels = channels == null ? Set.of() : Set.copyOf(channels);
        targetAudienceIds = targetAudienceIds == null ? Set.of() : Set.copyOf(targetAudienceIds);
    }

}
