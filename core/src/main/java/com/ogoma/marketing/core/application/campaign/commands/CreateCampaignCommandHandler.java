package com.ogoma.marketing.core.application.campaign.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.campaigns.CampaignConfiguration;
import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.sharedkernel.exceptions.DuplicateRecordException;

import java.time.Clock;
import java.util.Objects;

public record CreateCampaignCommandHandler(
        CampaignRepository campaignRepository,
        Clock clock,
        UnitOfWork unitOfWork) implements CommandHandler<CreateCampaignCommand, CampaignID> {
    @Override
    public Class<CreateCampaignCommand> supports() {
        return CreateCampaignCommand.class;
    }

    @Override
    public CampaignID handle(CreateCampaignCommand command) {
        Objects.requireNonNull(command, "command must not be null");
        if (campaignRepository.existsByName(command.name())) {
            throw new DuplicateRecordException("Campaign with name %s already exist".formatted(command.name()));
        }
        return unitOfWork.execute(() -> {
            CampaignConfiguration configuration = new CampaignConfiguration(
                    command.channels(),
                    command.emailTemplateID(),
                    command.smsTemplateID()
            );
            CampaignEntity campaignEntity = CampaignEntity.createNew(
                    command.name(),
                    command.description(),
                    command.targetAudienceIds(),
                    command.targetSegmentIds(),
                    configuration,
                    command.userId(),
                    clock);
            campaignRepository.save(campaignEntity);
            return campaignEntity.getId();
        });
    }
}
