package com.ogoma.marketing.core.application.audience.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.audience.AudienceEntity;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;


public record UpdateAudienceCommandHandler(AudienceRepository audienceRepository) implements CommandHandler<UpdateAudienceCommand, Void> {
    @Override
    public Class<UpdateAudienceCommand> supports() {
        return UpdateAudienceCommand.class;
    }

    @Override
    public Void handle(UpdateAudienceCommand command) {
        AudienceEntity audience = this.audienceRepository.findById(command.audienceId()).orElseThrow(() -> new RecordNotFoundException("Audience not found"));
        audience.update(command.name(), command.userId());
        this.audienceRepository.save(audience);
        return null;
    }
}
