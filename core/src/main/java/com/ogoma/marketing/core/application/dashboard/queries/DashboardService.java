package com.ogoma.marketing.core.application.dashboard.queries;

import java.util.List;

public interface DashboardService {

     GetStatsQueryView getStats();

     List<GetCampaignsByChannelCountView> getCampaignsByChannelCount();

}
