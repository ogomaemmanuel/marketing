package com.ogoma.marketing.core.application.engagement;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagement;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagementRepository;

import java.time.Clock;

public record RecordEngagementCommandHandler(
        Clock clock,
        CampaignEngagementRepository campaignEngagementRepository
) implements CommandHandler<RecordEngagementCommand, Void> {
    @Override
    public Class<RecordEngagementCommand> supports() {
        return RecordEngagementCommand.class;
    }

    @Override
    public Void handle(RecordEngagementCommand command) {
        CampaignEngagement campaignEngagement = CampaignEngagement.createNew(
                command.trackingId(),
                command.engagementType(),
                clock
        );
        this.campaignEngagementRepository.save(campaignEngagement);
        return null;
    }
}
