package com.ogoma.marketing.core.application.audience.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.audience.AudienceEntity;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import com.ogoma.marketing.core.domain.audience.TestAudience;
import com.ogoma.marketing.core.domain.audience.TestAudienceRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import org.jspecify.annotations.NonNull;

import java.time.Clock;

public record SetTestAudienceCommandHandler(
        AudienceRepository audienceRepository,
        TestAudienceRepository testAudienceRepository,
        Clock clock,
        UnitOfWork unitOfWork
) implements CommandHandler<SetTestAudienceCommand, Void> {

    @Override
    public @NonNull Class<SetTestAudienceCommand> supports() {
        return SetTestAudienceCommand.class;
    }

    @Override
    public Void handle(@NonNull SetTestAudienceCommand command) {
        return unitOfWork.execute(() -> {
            var existingTestAudienceOpt = this.testAudienceRepository.findFirst();
            // Exit early if the requested audience is already the active test audience
            if (existingTestAudienceOpt.isPresent()
                    && command.audienceId().equals(existingTestAudienceOpt.get().getAudienceId())) {
                return null;
            }
            // Validate and fetch the target audience before making any state changes
            AudienceEntity newAudience = this.audienceRepository.findById(command.audienceId())
                    .orElseThrow(() -> new RecordNotFoundException(
                            "Audience not found with id %s".formatted(command.audienceId().id())
                    ));
            // Unmark the previous test audience and remove/update the TestAudience record
            existingTestAudienceOpt.ifPresent(existingTestAudience -> {
                this.audienceRepository.findById(existingTestAudience.getAudienceId())
                        .ifPresent(previousAudience -> {
                            previousAudience.markAsNonTest(clock, command.userId());
                            this.audienceRepository.save(previousAudience);
                        });
                this.testAudienceRepository.delete(existingTestAudience);
            });

            // Mark and save the new test audience
            newAudience.markAsTest(clock, command.userId());
            this.audienceRepository.save(newAudience);
            TestAudience newTestAudience = new TestAudience(command.audienceId());
            this.testAudienceRepository.save(newTestAudience);
            return null;
        });
    }
}
