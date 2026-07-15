package com.ogoma.marketing.infrastructure.audience;


import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipEntity;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipRepository;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import org.springframework.data.core.PropertyPath;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void replaceMemberships(ContactID contactId, Set<AudienceId> desiredAudienceIds) {
        Query query = Query.query(Criteria.where(PropertyPath.of(AudienceMembershipEntity::getContactID)).is(contactId));
        List<AudienceMembershipEntity> membershipEntities = this.jdbcAggregateTemplate.findAll(query, AudienceMembershipEntity.class);
        Set<AudienceId> existingIds =
                membershipEntities.stream()
                        .map(AudienceMembershipEntity::getAudienceId)
                        .collect(Collectors.toSet());
        if (existingIds.equals(desiredAudienceIds)) {
            return;
        }
        Set<AudienceId> idsToAdd = new HashSet<>(desiredAudienceIds);
        idsToAdd.removeAll(existingIds);

        Set<AudienceId> idsToRemove = new HashSet<>(existingIds);
        idsToRemove.removeAll(desiredAudienceIds);

        if (!idsToRemove.isEmpty()) {
            List<AudienceMembershipEntity> toDelete = membershipEntities.stream()
                    .filter(m -> idsToRemove.contains(m.getAudienceId()))
                    .toList();
            jdbcAggregateTemplate.deleteAll(toDelete);
        }

        if (!idsToAdd.isEmpty()) {
            List<AudienceMembershipEntity> toInsert = idsToAdd.stream()
                    .map(id -> AudienceMembershipEntity.join(contactId, id))
                    .toList();
            jdbcAggregateTemplate.insertAll(toInsert);
        }
    }

    @Override
    public List<AudienceMembershipEntity> saveAll(Iterable<AudienceMembershipEntity> audienceMembershipEntity) {
        return jdbcAggregateTemplate.saveAll(audienceMembershipEntity);
    }
}
