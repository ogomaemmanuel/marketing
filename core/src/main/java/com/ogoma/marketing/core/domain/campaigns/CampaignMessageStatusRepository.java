package com.ogoma.marketing.core.domain.campaigns;

import java.util.UUID;

public interface CampaignMessageStatusRepository {
    CampaignMessageStatus save(CampaignMessageStatus campaignMessageStatus);

    CampaignMessageStatus findById(CampaignMessageStatusID campaignMessageStatusID);
    Boolean existByTrackingId(UUID trackingId);
}
