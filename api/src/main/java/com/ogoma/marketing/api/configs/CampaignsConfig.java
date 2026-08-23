package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.application.campaign.commands.CreateCampaignCommandHandler;
import com.ogoma.marketing.core.application.campaign.queries.GetCampaignByIDQueryHandler;
import com.ogoma.marketing.core.application.campaign.queries.GetCampaignsQueryHandler;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CampaignsConfig {
    @Bean
    CreateCampaignCommandHandler createCampaignCommandHandler(CampaignRepository campaignRepository, UnitOfWork unitOfWork) {
        return new CreateCampaignCommandHandler(campaignRepository, unitOfWork);
    }
    @Bean
    GetCampaignByIDQueryHandler getCampaignByIDQueryHandler(CampaignRepository campaignRepository) {
        return new GetCampaignByIDQueryHandler(campaignRepository);
    }
    @Bean
    GetCampaignsQueryHandler getCampaignsQueryHandler(CampaignRepository campaignRepository) {
        return new GetCampaignsQueryHandler(campaignRepository);
    }

}
