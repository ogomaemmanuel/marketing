package com.ogoma.marketing.core.application.engagement;

public record GetEngagementAnalyticsView(
        long totalClicks,
        long uniqueClicks,
        long sent,
        long delivered,
        long bounced,
        long uniqueOpens,
        long totalOpens

) {


}
