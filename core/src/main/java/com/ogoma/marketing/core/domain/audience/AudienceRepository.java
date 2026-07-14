package com.ogoma.marketing.core.domain.audience;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface AudienceRepository {
    AudienceEntity save(AudienceEntity audienceEntity);

    Optional<AudienceEntity> findById(AudienceId audienceID);
    Set<AudienceId> findStaticAudienceIds(Collection<AudienceId> audienceIds);
    Page<AudienceEntity> findAllBy(String searchTerm, Pageable pageable);
}
