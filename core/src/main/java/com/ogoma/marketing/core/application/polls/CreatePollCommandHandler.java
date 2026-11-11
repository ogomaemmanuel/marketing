package com.ogoma.marketing.core.application.polls;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.polls.entities.Poll;
import com.ogoma.marketing.core.domain.polls.repository.PollRepository;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;
import com.ogoma.marketing.core.sharedkernel.exceptions.DuplicateRecordException;
import org.jspecify.annotations.NonNull;

import java.time.Clock;

public record CreatePollCommandHandler(
        PollRepository pollRepository,
        UnitOfWork unitOfWork,
        Clock clock
) implements CommandHandler<CreatePollCommand, PollID> {
    @Override
    public @NonNull Class<CreatePollCommand> supports() {
        return CreatePollCommand.class;
    }

    @Override
    public PollID handle(@NonNull CreatePollCommand command) {
        if (Boolean.TRUE.equals(this.pollRepository.existsByName(command.name()))) {
            throw new DuplicateRecordException("Poll with name %s already exists".formatted(command.name()));
        }
        return unitOfWork.execute(() -> {
            var poll = Poll.createNew(
                    command.name(),
                    command.question(),
                    command.questionDescription(),
                    command.redirectURL(),
                    command.options(),
                    command.orientation(),
                    command.listStyle(),
                    command.startTime(),
                    command.endTime(),
                    command.createdBy(),
                    clock
            );
            this.pollRepository.save(poll);
            return poll.getId();
        });

    }
}
