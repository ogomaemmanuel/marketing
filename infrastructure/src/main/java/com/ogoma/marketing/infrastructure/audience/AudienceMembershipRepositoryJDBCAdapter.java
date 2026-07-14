package com.ogoma.marketing.infrastructure.audience;

import com.ogoma.marketing.core.domain.contacts.AudienceMembershipEntity;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipRepository;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record AudienceMembershipRepositoryJDBCAdapter(
        JdbcAggregateTemplate jdbcAggregateTemplate,
        AudienceJDBCRepository jdbcRepository
) implements AudienceMembershipRepository {
    @Override
    public AudienceMembershipEntity save(AudienceMembershipEntity audienceMembershipEntity) {
        return jdbcAggregateTemplate.save(audienceMembershipEntity);
    }

    @Override
    public List<AudienceMembershipEntity> saveAll(Iterable<AudienceMembershipEntity> audienceMembershipEntity) {
        return jdbcAggregateTemplate.saveAll(audienceMembershipEntity);
    }
}
