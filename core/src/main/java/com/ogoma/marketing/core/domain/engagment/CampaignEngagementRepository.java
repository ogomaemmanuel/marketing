package com.ogoma.marketing.core.domain.engagment;

import com.ogoma.marketing.core.application.engagement.GetCampaignComparisonView;
import com.ogoma.marketing.core.application.engagement.GetEngagementAnalyticsView;

public interface CampaignEngagementRepository {
    CampaignEngagement save(CampaignEngagement campaignEngagement);

    GetEngagementAnalyticsView getEngagementAnalytics();

    GetCampaignComparisonView getCampaignComparisonData();
}
