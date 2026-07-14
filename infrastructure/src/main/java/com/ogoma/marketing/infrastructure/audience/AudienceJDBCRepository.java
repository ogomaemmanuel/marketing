package com.ogoma.marketing.infrastructure.audience;

import com.ogoma.marketing.core.domain.audience.AudienceEntity;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface AudienceJDBCRepository extends CrudRepository<AudienceEntity, AudienceId> {

    Set<AudienceIDOnly> findAllByIdIn(Collection<AudienceId> audienceId);
}
