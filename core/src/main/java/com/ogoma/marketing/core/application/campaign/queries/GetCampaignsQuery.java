package com.ogoma.marketing.core.application.campaign.queries;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record GetCampaignsQuery(
        String searchTerm,
        Pageable pageable

) implements Query<Page<GetCampaignsView>> {


}
