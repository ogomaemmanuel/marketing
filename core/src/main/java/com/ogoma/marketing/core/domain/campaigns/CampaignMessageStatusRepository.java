package com.ogoma.marketing.core.domain.campaigns;

public interface CampaignMessageStatusRepository {
    CampaignMessageStatus save(CampaignMessageStatus campaignMessageStatus);

    CampaignMessageStatus findById(CampaignMessageStatusID campaignMessageStatusID);
}
