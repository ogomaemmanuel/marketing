package com.ogoma.marketing.core.application.audience.services;

import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.segments.SegmentID;

import java.util.List;

public interface AudienceMatcher {
    Long count(List<AudienceId> audience);

    List<ContactID> match(List<AudienceId> audience, List<SegmentID> segmentIDS);
}
