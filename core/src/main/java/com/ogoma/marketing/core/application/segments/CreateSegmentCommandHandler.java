package com.ogoma.marketing.core.application.segments;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.audience.AudienceEntity;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;

public record CreateSegmentCommandHandler(
        AudienceRepository audienceRepository) implements CommandHandler<CreateSegmentCommand, AudienceId> {
    @Override
    public Class<CreateSegmentCommand> supports() {
        return CreateSegmentCommand.class;
    }

    @Override
    public AudienceId handle(CreateSegmentCommand command) {
        var audience = AudienceEntity.createDynamicAudience(
                command.name(),
                command.userId(), command.ruleSet());
        audienceRepository.save(audience);
        return audience.getId();
    }
}
