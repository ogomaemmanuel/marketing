package com.ogoma.marketing.infrastructure.engagement;

import com.ogoma.marketing.core.application.engagement.GetEngagementAnalyticsView;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagement;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagementRepository;
import com.ogoma.marketing.core.domain.engagment.EngagementType;
import com.ogoma.marketing.infrastructure.jooq.tables.CampaignEngagements;
import com.ogoma.marketing.infrastructure.jooq.tables.CampaignMessageStatus;
import org.jooq.DSLContext;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Repository;

import static org.jooq.impl.DSL.*;


@Repository
public record CampaignEngagementRepositoryAdapter(
        DSLContext dslContext,
        JdbcAggregateTemplate aggregateTemplate) implements CampaignEngagementRepository {
    @Override
    public CampaignEngagement save(CampaignEngagement campaignEngagement) {
        return this.aggregateTemplate.save(campaignEngagement);
    }

    @Override
    public GetEngagementAnalyticsView getEngagementAnalytics() {
        var eg = CampaignEngagements.CAMPAIGN_ENGAGEMENTS.as("eg");
        var cms = CampaignMessageStatus.CAMPAIGN_MESSAGE_STATUS.as("cms");
        // 1. Global delivery statistics (single row)
        var deliveryStats = dslContext
                .select(
                        countDistinct(cms.TRACKING_ID)
                                .filterWhere(cms.STATUS.in( com.ogoma.marketing.core.domain.campaigns.CampaignMessageStatus.Status.DELIVERED.name(), com.ogoma.marketing.core.domain.campaigns.CampaignMessageStatus.Status.BOUNCED.name()))
                                .as("sent"),
                        countDistinct(cms.TRACKING_ID)
                                .filterWhere(cms.STATUS.equalIgnoreCase(com.ogoma.marketing.core.domain.campaigns.CampaignMessageStatus.Status.DELIVERED.name()))
                                .as("delivered"),
                        countDistinct(cms.TRACKING_ID)
                                .filterWhere(cms.STATUS.equalIgnoreCase(com.ogoma.marketing.core.domain.campaigns.CampaignMessageStatus.Status.BOUNCED.name()))
                                .as("bounced")
                )
                .from(cms)
                .asTable("d");

        // 2. Global engagement statistics (single row)
        var engagementStats = dslContext
                .select(
                        count(eg.TRACKING_ID)
                                .filterWhere(eg.ENGAGEMENT_TYPE.equalIgnoreCase(EngagementType.OPEN.name()))
                                .as("total_opens"),
                        countDistinct(eg.TRACKING_ID)
                                .filterWhere(eg.ENGAGEMENT_TYPE.equalIgnoreCase(EngagementType.OPEN.name()))
                                .as("unique_opens"),
                        countDistinct(eg.TRACKING_ID)
                                .filterWhere(eg.ENGAGEMENT_TYPE.equalIgnoreCase(EngagementType.CLICK.name()))
                                .as("unique_clicks"),
                        count(eg.TRACKING_ID)
                                .filterWhere(eg.ENGAGEMENT_TYPE.equalIgnoreCase(EngagementType.CLICK.name()))
                                .as("total_clicks")
                )
                .from(eg)
                .asTable("e");

        // 3. Cross join and map fields in the exact order of GetEngagementAnalyticsView components:
        // (totalClicks, uniqueClicks, sent, delivered, bounced, uniqueOpens, totalOpens)
        return dslContext.select(
                        engagementStats.field("total_clicks"),
                        engagementStats.field("unique_clicks"),
                        deliveryStats.field("sent"),
                        deliveryStats.field("delivered"),
                        deliveryStats.field("bounced"),
                        engagementStats.field("unique_opens"),
                        engagementStats.field("total_opens")
                )
                .from(deliveryStats)
                .crossJoin(engagementStats)
                .fetchAnyInto(GetEngagementAnalyticsView.class);
    }
}
