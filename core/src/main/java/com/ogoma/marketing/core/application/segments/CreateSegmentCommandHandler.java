package com.ogoma.marketing.core.application.segments;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.segments.Segment;
import com.ogoma.marketing.core.domain.segments.SegmentID;
import com.ogoma.marketing.core.domain.segments.SegmentRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

@Slf4j
public record CreateSegmentCommandHandler(SegmentRepository segmentRepository, Clock clock) implements CommandHandler<CreateSegmentCommand, SegmentID> {
    @Override
    public Class<CreateSegmentCommand> supports() {
        return CreateSegmentCommand.class;
    }

    @Override
    public SegmentID handle(CreateSegmentCommand command) {
        Segment segment = Segment.createNew(command.name(),command.description(), command.ruleSet(), command.userId(), clock);
        log.info("Sql ruleset {}",segment.toSql("test"));
        segmentRepository.save(segment);
        return segment.getId();
    }
}
