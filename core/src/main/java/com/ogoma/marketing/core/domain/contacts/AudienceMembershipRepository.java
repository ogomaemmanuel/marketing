package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.domain.audience.AudienceId;

import java.util.List;
import java.util.Set;

public interface AudienceMembershipRepository {
    AudienceMembershipEntity save(AudienceMembershipEntity audienceMembershipEntity);
    void replaceMemberships(ContactID contactId, Set<AudienceId> audienceIds);
    List<AudienceMembershipEntity> saveAll(Iterable<AudienceMembershipEntity> audienceMembershipEntity);
}
