package com.ogoma.marketing.infrastructure.audience;

import com.ogoma.marketing.core.application.audience.services.AudienceMatcher;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.segments.Segment;
import com.ogoma.marketing.core.domain.segments.SegmentID;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record PostgresAudienceMatcher(
        JdbcAggregateTemplate jdbcAggregateTemplate,
        JdbcClient jdbcClient) implements AudienceMatcher {
    @Override
    public Long count(List<AudienceId> audience) {
        return null;
    }

    @Override
    public Stream<ContactID> match(Set<AudienceId> audienceIds, Set<SegmentID> segmentIDS) {
        List<String> queryParts = new ArrayList<>();
        Map<String, Object> aggregatedParams = new HashMap<>();
        if (audienceIds != null && !audienceIds.isEmpty()) {
            queryParts.add("select am.contact_id as contact_id from  audience_membership am where audience_id in (:audienceIds)");
            aggregatedParams.put("audienceIds", extractAudienceRawIds(audienceIds));
        }
        if (segmentIDS != null && !segmentIDS.isEmpty()) {
            List<Segment> segments = jdbcAggregateTemplate.findAllById(segmentIDS, Segment.class);
            for (int i = 0; i < segments.size(); i++) {
                var sqlFragmentWithParams = segments.get(i).getRuleSet().toNamedSQL(String.valueOf("seg_" + i + "_"));
                queryParts.add("select id as contact_id from contacts where " + sqlFragmentWithParams.sql()); //dynamic audience
                aggregatedParams.putAll(sqlFragmentWithParams.params());
            }
        }
        if (queryParts.isEmpty()) {
            return Stream.of();
        }
        var query = String.join(" Union ", queryParts);
        return jdbcClient.sql(query)
                .params(aggregatedParams)
                .query(ContactID.class)
                .stream();
    }

    private Set<UUID> extractAudienceRawIds(Set<AudienceId> audience) {
        if (audience == null) return Set.of();
        return audience.stream().map(AudienceId::id).collect(Collectors.toSet());
    }
}
