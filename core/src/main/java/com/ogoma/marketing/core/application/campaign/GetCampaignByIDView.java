package com.ogoma.marketing.core.application.campaign;

import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;

public record GetCampaignByIDView() {

    public GetCampaignByIDView(CampaignEntity campaignEntity){
        this();
    }
}
