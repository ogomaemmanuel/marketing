package com.ogoma.marketing.infrastructure.dashboard;

import com.ogoma.marketing.core.application.dashboard.queries.DashboardService;
import com.ogoma.marketing.core.application.dashboard.queries.GetCampaignsByChannelCountView;
import com.ogoma.marketing.core.application.dashboard.queries.GetStatsQueryView;
import org.jooq.DSLContext;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ogoma.marketing.infrastructure.jooq.Tables.*;
import static org.jooq.impl.DSL.count;

@Component
public record DashboardServiceImpl(JdbcClient jdbcClient, DSLContext dslContext) implements DashboardService {
    @Override
    public GetStatsQueryView getStats() {
        return dslContext.select(
                        dslContext.selectCount().from(CONTACTS).asField("total_contacts"),
                        dslContext.selectCount().from(AUDIENCES).asField("total_audiences"),
                        dslContext.selectCount().from(CAMPAIGNS).asField("total_campaigns"),
                        dslContext.selectCount().from(SMS_TEMPLATES).asField("total_sms_templates")
                )
                .fetchSingleInto(GetStatsQueryView.class);

    }

    @Override
    public List<GetCampaignsByChannelCountView> getCampaignsByChannelCount() {
        return dslContext.select(
                        CAMPAIGN_CHANNELS.CHANNEL,
                        count(CAMPAIGN_CHANNELS.CAMPAIGN_ID).as("total_campaigns")
                )
                .from(CAMPAIGN_CHANNELS)
                .groupBy(CAMPAIGN_CHANNELS.CHANNEL)
                .orderBy(count(CAMPAIGN_CHANNELS.CAMPAIGN_ID).desc())
                .fetchInto(GetCampaignsByChannelCountView.class);

    }
}
