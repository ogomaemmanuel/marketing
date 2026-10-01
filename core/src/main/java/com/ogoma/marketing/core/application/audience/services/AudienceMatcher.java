package com.ogoma.marketing.core.application.audience.services;

import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.segments.SegmentID;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface AudienceMatcher {
    Long count(List<AudienceId> audience);

    Stream<ContactID> match(Set<AudienceId> audience, Set<SegmentID> segmentIDS);
}
