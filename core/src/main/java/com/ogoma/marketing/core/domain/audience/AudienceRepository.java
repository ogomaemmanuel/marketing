package com.ogoma.marketing.core.domain.audience;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AudienceRepository {
    AudienceEntity save(AudienceEntity audienceEntity);
    Optional<AudienceEntity> findById(AudienceId audienceID);
    Page<AudienceEntity> findAllBy(Pageable pageable);
}
