package com.ogoma.marketing.core.application.segments.queries;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.domain.segments.SegmentID;

public record GetSegmentByIDQuery(
        SegmentID segmentID
) implements Query<GetSegmentByIDView> {
}
