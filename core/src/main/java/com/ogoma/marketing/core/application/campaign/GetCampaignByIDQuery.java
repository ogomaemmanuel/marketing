package com.ogoma.marketing.core.application.campaign;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;

public record GetCampaignByIDQuery(CampaignID campaignID) implements Query<GetCampaignByIDView> {

}
