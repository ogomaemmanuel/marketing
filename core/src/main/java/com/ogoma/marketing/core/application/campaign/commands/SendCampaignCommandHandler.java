package com.ogoma.marketing.core.application.campaign.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;

import java.time.Clock;

public record SendCampaignCommandHandler(
        CampaignRepository campaignRepository,
        Clock clock
) implements CommandHandler<SendCampaignCommand, Void> {
    @Override
    public Class<SendCampaignCommand> supports() {
        return SendCampaignCommand.class;
    }

    @Override
    public Void handle(SendCampaignCommand command) {
        CampaignEntity campaignEntity = this.campaignRepository.findByID(command.id()).orElseThrow(() -> new RecordNotFoundException("Campaign not found with id %s".formatted(command.id())));
        campaignEntity.startSending(command.senderId(), clock);
        this.campaignRepository.save(campaignEntity);
        return null;
    }
}
