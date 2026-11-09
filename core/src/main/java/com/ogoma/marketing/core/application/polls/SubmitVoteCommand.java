package com.ogoma.marketing.core.application.polls;

import com.ogoma.marketing.core.domain.campaigns.CampaignID;

import java.util.UUID;

public record SubmitFeedbackCommand(
         CampaignID campaignID,
         UUID trackingId,
         String feedback
) {


}
