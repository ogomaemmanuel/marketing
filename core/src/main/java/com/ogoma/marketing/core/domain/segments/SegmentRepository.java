package com.ogoma.marketing.core.domain.segments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SegmentRepository {
    Segment save(Segment segment);

    Page<Segment> findSegments(Pageable pageable, String searchTerm);
}
