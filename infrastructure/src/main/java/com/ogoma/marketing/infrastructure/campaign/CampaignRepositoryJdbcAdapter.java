package com.ogoma.marketing.infrastructure.campaign;

import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public record CampaignRepositoryJdbcAdapter(
        JdbcAggregateTemplate jdbcAggregateTemplate
) implements CampaignRepository {

    @Override
    public CampaignEntity save(CampaignEntity campaignEntity) {
        return jdbcAggregateTemplate.save(campaignEntity);
    }

    @Override
    public Optional<CampaignEntity> findByID(CampaignID campaignID) {
        return Optional.ofNullable(jdbcAggregateTemplate.findById(campaignID, CampaignEntity.class));
    }
}
