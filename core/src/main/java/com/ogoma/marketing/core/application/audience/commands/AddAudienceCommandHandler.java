package com.ogoma.marketing.core.application.audience.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.audience.AudienceEntity;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;

public record AddAudienceCommandHandler(AudienceRepository audienceRepository) implements CommandHandler<AddAudienceCommand, AudienceEntity> {

    @Override
    public Class<AddAudienceCommand> supports() {
        return AddAudienceCommand.class;
    }

    @Override
    public AudienceEntity handle(AddAudienceCommand command) {
        AudienceEntity audienceEntity =  AudienceEntity.createNew(command.name(), command.userID());
        return this.audienceRepository.save(audienceEntity);
    }
}
