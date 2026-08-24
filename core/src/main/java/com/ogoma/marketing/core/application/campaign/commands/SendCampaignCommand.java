package com.ogoma.marketing.core.application.campaign.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;

public record SendCampaignCommand(
        CampaignID id,
        String senderId
) implements Command<Void> {

}
