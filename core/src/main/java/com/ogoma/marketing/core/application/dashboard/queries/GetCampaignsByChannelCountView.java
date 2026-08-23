package com.ogoma.marketing.core.application.dashboard.queries;

import com.ogoma.marketing.core.domain.campaigns.Channel;

public record GetCampaignsByChannelCountView(
        Channel channel,
        Long totalCampaigns
) {

}
