package com.ogoma.marketing.core.domain.segments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SegmentRepository {
    Segment save(Segment segment);

    Page<Segment> findSegments(Pageable pageable, String searchTerm);
    Optional<Segment> findByID(SegmentID segmentID);
}
