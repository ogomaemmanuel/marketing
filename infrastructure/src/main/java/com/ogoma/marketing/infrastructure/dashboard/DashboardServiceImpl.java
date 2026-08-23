package com.ogoma.marketing.infrastructure.dashboard;

import com.ogoma.marketing.core.application.dashboard.queries.DashboardService;
import com.ogoma.marketing.core.application.dashboard.queries.GetCampaignsByChannelCountView;
import com.ogoma.marketing.core.application.dashboard.queries.GetStatsQueryView;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public record DashboardServiceImpl(JdbcClient jdbcClient) implements DashboardService {
    @Override
    public GetStatsQueryView getStats() {
        String sql = """
                SELECT
                    (SELECT COUNT(*) FROM contacts) AS total_contacts,
                    (SELECT COUNT(*) FROM audiences) AS total_audiences,
                    (SELECT COUNT(*) FROM campaigns) AS total_campaigns,
                    (SELECT COUNT(*) FROM sms_templates) AS total_sms_templates
                """;
        return jdbcClient.sql(sql).query(GetStatsQueryView.class).single();

    }

    @Override
    public Set<GetCampaignsByChannelCountView> getCampaignsByChannelCount() {
        return jdbcClient.sql("""
                SELECT\s
                    channel,
                    COUNT(id) AS total_campaigns
                FROM marketing_main.campaigns,
                UNNEST(channels) AS channel
                GROUP BY channel
                ORDER BY total_campaigns DESC
                """).query(GetCampaignsByChannelCountView.class).set();
    }
}
