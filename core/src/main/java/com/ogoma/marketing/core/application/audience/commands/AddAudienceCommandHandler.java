package com.ogoma.marketing.core.application.audience.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.audience.AudienceEntity;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import com.ogoma.marketing.core.sharedkernel.exceptions.DuplicateRecordException;

public record AddAudienceCommandHandler(
        AudienceRepository audienceRepository
) implements CommandHandler<AddAudienceCommand, AudienceEntity> {

    @Override
    public Class<AddAudienceCommand> supports() {
        return AddAudienceCommand.class;
    }

    @Override
    public AudienceEntity handle(AddAudienceCommand command) {
        if (this.audienceRepository.existsByName(command.name())) {
            throw new DuplicateRecordException("Audience with %s already exist".formatted(command.name()));
        }
        AudienceEntity audienceEntity = AudienceEntity.createStaticAudience(command.name(), command.userID());
        return this.audienceRepository.save(audienceEntity);
    }
}
