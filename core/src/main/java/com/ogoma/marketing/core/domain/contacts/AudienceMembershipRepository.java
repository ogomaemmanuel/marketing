package com.ogoma.marketing.core.domain.contacts;

import java.util.List;

public interface AudienceMembershipRepository {
    AudienceMembershipEntity save(AudienceMembershipEntity audienceMembershipEntity);


    List<AudienceMembershipEntity> saveAll(Iterable<AudienceMembershipEntity> audienceMembershipEntity);
}
