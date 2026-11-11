package com.ogoma.marketing.infrastructure.engagement;

import com.ogoma.marketing.core.application.engagement.GetCampaignComparisonView;
import com.ogoma.marketing.core.application.engagement.GetEngagementAnalyticsView;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagement;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagementRepository;
import com.ogoma.marketing.core.domain.engagment.EngagementType;
import com.ogoma.marketing.infrastructure.jooq.tables.CampaignEngagements;
import com.ogoma.marketing.infrastructure.jooq.tables.CampaignMessageStatus;
import com.ogoma.marketing.infrastructure.jooq.tables.Campaigns;
import org.jooq.DSLContext;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

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
    public GetEngagementAnalyticsView getEngagementAnalytics(Set<UUID> campaignIds) {
        var eg = CampaignEngagements.CAMPAIGN_ENGAGEMENTS.as("eg");
        var cms = CampaignMessageStatus.CAMPAIGN_MESSAGE_STATUS.as("cms");
        var campaignFilter = campaignIds == null || campaignIds.isEmpty()
                ? noCondition()
                : cms.CAMPAIGN_ID.in(campaignIds);
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
                .where(campaignFilter)
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
                .where(campaignIds == null || campaignIds.isEmpty()
                        ? noCondition()
                        : eg.TRACKING_ID.in(select(cms.TRACKING_ID).from(cms).where(campaignFilter)))
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

    @Override
    public GetCampaignComparisonView getCampaignComparisonData() {
        var campaigns = Campaigns.CAMPAIGNS.as("c");
        var cms = CampaignMessageStatus.CAMPAIGN_MESSAGE_STATUS.as("cms");
        var eg = CampaignEngagements.CAMPAIGN_ENGAGEMENTS.as("eg");

        var deliveredStatus = com.ogoma.marketing.core.domain.campaigns.CampaignMessageStatus.Status.DELIVERED.name();
        var openType = EngagementType.OPEN.name();
        var clickType = EngagementType.CLICK.name();

        var deliveryStats = dslContext
                .select(
                        cms.CAMPAIGN_ID.as("campaign_id"),
                        countDistinct(cms.TRACKING_ID)
                                .filterWhere(cms.STATUS.equalIgnoreCase(deliveredStatus))
                                .as("deliveries")
                )
                .from(cms)
                .groupBy(cms.CAMPAIGN_ID)
                .asTable("delivery_stats");

        var engagementStats = dslContext
                .select(
                        cms.CAMPAIGN_ID.as("campaign_id"),
                        countDistinct(eg.TRACKING_ID)
                                .filterWhere(eg.ENGAGEMENT_TYPE.equalIgnoreCase(openType))
                                .as("opens"),
                        countDistinct(eg.TRACKING_ID)
                                .filterWhere(eg.ENGAGEMENT_TYPE.equalIgnoreCase(clickType))
                                .as("clicks")
                )
                .from(cms)
                .join(eg).on(eg.TRACKING_ID.eq(cms.TRACKING_ID))
                .groupBy(cms.CAMPAIGN_ID)
                .asTable("engagement_stats");

        var performance = dslContext
                .select(
                        campaigns.ID.cast(String.class).as("campaignId"),
                        campaigns.NAME.as("campaignName"),
                        coalesce(deliveryStats.field("deliveries", Long.class), 0L).as("deliveries"),
                        coalesce(engagementStats.field("opens", Long.class), 0L).as("opens"),
                        coalesce(engagementStats.field("clicks", Long.class), 0L).as("clicks")
                )
                .from(campaigns)
                .leftJoin(deliveryStats)
                .on(deliveryStats.field("campaign_id", UUID.class).eq(campaigns.ID))
                .leftJoin(engagementStats)
                .on(engagementStats.field("campaign_id", UUID.class).eq(campaigns.ID))
                .fetchInto(GetCampaignComparisonView.CampaignPerformanceDto.class);
        return new GetCampaignComparisonView(performance);
    }


}
