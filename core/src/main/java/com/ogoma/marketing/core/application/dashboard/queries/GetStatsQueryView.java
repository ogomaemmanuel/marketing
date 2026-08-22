package com.ogoma.marketing.core.application.dashboard.queries;

public record GetStatsQueryView(

        long totalContacts,
        long totalAudiences,
        long totalCampaigns,
        long totalSmsTemplates
) {

}
