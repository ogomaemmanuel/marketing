package com.ogoma.marketing.core.domain.campaigns;

import java.util.Optional;

public interface CampaignRepository {

    CampaignEntity save(CampaignEntity campaignEntity);

    Optional<CampaignEntity> findByID(CampaignID campaignID);
}
