package com.ogoma.marketing.core.application.segments.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.segments.SegmentRepository;
import org.springframework.data.domain.Page;

public record GetSegmentsQueryHandler(
        SegmentRepository segmentRepository
) implements QueryHandler<GetSegmentsQuery, Page<GetSegmentsListItemView>> {
    @Override
    public Class<GetSegmentsQuery> supports() {
        return GetSegmentsQuery.class;
    }

    @Override
    public Page<GetSegmentsListItemView> handle(GetSegmentsQuery query) {
        return this.segmentRepository.findSegments(query.pageable(), query.searchTerm())
                .map(segment -> new GetSegmentsListItemView(
                        segment.getId().id(),
                        segment.getName(),
                        segment.getDescription(),
                        segment.getCreatedAt()
                ));
    }
}
