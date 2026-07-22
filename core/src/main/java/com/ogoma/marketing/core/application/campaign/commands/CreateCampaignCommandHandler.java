package com.ogoma.marketing.core.application.campaign.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.campaigns.CampaignConfiguration;
import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;

public record CreateCampaignCommandHandler(CampaignRepository campaignRepository,
                                           UnitOfWork unitOfWork) implements CommandHandler<CreateCampaignCommand, CampaignID> {
    @Override
    public Class<CreateCampaignCommand> supports() {
        return CreateCampaignCommand.class;
    }

    @Override
    public CampaignID handle(CreateCampaignCommand command) {
        return unitOfWork.execute(() -> {
            CampaignConfiguration configuration = new CampaignConfiguration(
                    command.channels(),
                    command.emailTemplateID(),
                    command.smsTemplateID()
            );
            CampaignEntity campaignEntity = CampaignEntity.createNew(command.name(),
                    command.description(), command.targetAudienceIds(), configuration, command.userId());
            campaignRepository.save(campaignEntity);
            return campaignEntity.getId();
        });
    }
}
