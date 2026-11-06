package com.ogoma.marketing.core.application.segments.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.segments.Segment;
import com.ogoma.marketing.core.domain.segments.SegmentID;
import com.ogoma.marketing.core.domain.segments.SegmentRepository;
import com.ogoma.marketing.core.sharedkernel.exceptions.DuplicateRecordException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.time.Clock;

@Slf4j
public record CreateSegmentCommandHandler(SegmentRepository segmentRepository,
                                          Clock clock) implements CommandHandler<CreateSegmentCommand, SegmentID> {
    @Override
    public @NonNull Class<CreateSegmentCommand> supports() {
        return CreateSegmentCommand.class;
    }

    @Override
    public SegmentID handle(@NonNull CreateSegmentCommand command) {
        if (segmentRepository.existsByName(command.name())) {
            throw new DuplicateRecordException("Segment with %s name already exist".formatted(command.name()));
        }
        Segment segment = Segment.createNew(command.name(), command.description(), command.ruleSet(), command.userId(), clock);
        log.info("Sql ruleset {}", segment.toSql("test"));
        segmentRepository.save(segment);
        return segment.getId();
    }
}
