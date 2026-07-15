package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;

import java.util.HashSet;
import java.util.Set;

public record AudienceMembershipValidator(
        AudienceRepository audienceRepository
) {
    public void validateManualMembership(
            Set<AudienceId> audienceIds) {

        if (audienceIds.isEmpty()) {
            return;
        }

        Set<AudienceId> staticIds =
                audienceRepository.findStaticAudienceIds(audienceIds);


        Set<AudienceId> invalidIds =
                new HashSet<>(audienceIds);

        invalidIds.removeAll(staticIds);


        if (!invalidIds.isEmpty()) {
            throw new InvalidAudienceException("Invalid audiences: " +
                    invalidIds);
        }
    }
}
