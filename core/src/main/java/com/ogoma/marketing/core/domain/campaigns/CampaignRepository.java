package com.ogoma.marketing.core.domain.campaigns;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CampaignRepository {

    CampaignEntity save(CampaignEntity campaignEntity);

    Optional<CampaignEntity> findByID(CampaignID campaignID);
    Page<CampaignEntity> findAllBy(String searchTerm,Pageable pageable);
}
