package com.ogoma.marketing.core.application.dashboard.queries;

import java.util.Set;

public interface DashboardService {

     GetStatsQueryView getStats();

     Set<GetCampaignsByChannelCountView> getCampaignsByChannelCount();

}
