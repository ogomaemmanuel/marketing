package com.ogoma.marketing.core.domain.engagment;

import com.ogoma.marketing.core.application.engagement.GetCampaignComparisonView;
import com.ogoma.marketing.core.application.engagement.GetEngagementAnalyticsView;

import java.util.Set;
import java.util.UUID;

public interface CampaignEngagementRepository {
    CampaignEngagement save(CampaignEngagement campaignEngagement);

    /** Empty campaignIds means all campaigns. */
    GetEngagementAnalyticsView getEngagementAnalytics(Set<UUID> campaignIds);

    GetCampaignComparisonView getCampaignComparisonData();
}
