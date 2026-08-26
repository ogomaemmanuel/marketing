package com.ogoma.marketing.core.application.segments.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import com.ogoma.marketing.core.domain.segments.SegmentRepository;

public record GetSegmentByIDQueryHandler(
        SegmentRepository segmentRepository
) implements QueryHandler<GetSegmentByIDQuery, GetSegmentByIDView> {
    @Override
    public Class<GetSegmentByIDQuery> supports() {
        return GetSegmentByIDQuery.class;
    }

    @Override
    public GetSegmentByIDView handle(GetSegmentByIDQuery query) {
        return this.segmentRepository.findByID(query.segmentID()).map(segment->new GetSegmentByIDView(
               segment.getName(),
               segment.getCreatedBy(),
               segment.getCreatedAt(),
               segment.getRuleSet()
        )).orElseThrow(()->new RecordNotFoundException("Segment not found with id %s".formatted(query.segmentID())));
    }
}
