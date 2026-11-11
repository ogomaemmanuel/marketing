package com.ogoma.marketing.core.application.polls;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.campaigns.CampaignMessageStatusRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import com.ogoma.marketing.core.domain.polls.entities.Poll;
import com.ogoma.marketing.core.domain.polls.entities.PollVote;
import com.ogoma.marketing.core.domain.polls.entities.PollVoteID;
import com.ogoma.marketing.core.domain.polls.repository.PollRepository;
import com.ogoma.marketing.core.domain.polls.repository.PollVoteRepository;
import com.ogoma.marketing.core.sharedkernel.exceptions.DuplicateRecordException;
import org.jspecify.annotations.NonNull;

import java.time.Clock;

public record SubmitVoteCommandHandler(
        PollVoteRepository pollVoteRepository,

        PollRepository pollRepository,

        CampaignMessageStatusRepository campaignMessageStatusRepository,
        UnitOfWork unitOfWork,
        Clock clock
) implements CommandHandler<SubmitVoteCommand, PollVoteID> {
    @Override
    public @NonNull Class<SubmitVoteCommand> supports() {
        return SubmitVoteCommand.class;
    }

    @Override
    public PollVoteID handle(@NonNull SubmitVoteCommand command) {
        return unitOfWork.execute(() -> {
            if (!campaignMessageStatusRepository.existByTrackingId(command.trackingId())) {
                throw new RecordNotFoundException("Tracking id %s does not exist".formatted(command.trackingId()));
            }
            if (pollVoteRepository.existByTrackingId(command.trackingId())) {
                throw new DuplicateRecordException("Your vote is already submitted");
            }
            Poll poll = pollRepository.findByID(command.pollID()).orElseThrow(() -> new RecordNotFoundException("Poll with id %s does not exist".formatted(command.pollID().id())));
            poll.validateCanAcceptVotes(clock);
            poll.validateOption(command.feedback());
            PollVote pollVote = new PollVote(
                    command.campaignID(),
                    command.pollID(),
                    command.feedback(),
                    command.trackingId(),
                    clock
            );
            this.pollVoteRepository.save(pollVote);
            return pollVote.getId();
        });
    }
}
