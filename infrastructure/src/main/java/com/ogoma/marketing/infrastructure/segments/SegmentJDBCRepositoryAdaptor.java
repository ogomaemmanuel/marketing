package com.ogoma.marketing.infrastructure.segments;

import com.ogoma.marketing.core.domain.segments.Segment;
import com.ogoma.marketing.core.domain.segments.SegmentID;
import com.ogoma.marketing.core.domain.segments.SegmentRepository;
import com.ogoma.marketing.core.sharedkernel.Entity;
import org.springframework.data.core.PropertyPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository

public class SegmentJDBCRepositoryAdaptor implements SegmentRepository {
    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    public SegmentJDBCRepositoryAdaptor(JdbcAggregateTemplate jdbcAggregateTemplate) {
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
    }

    @Override
    public Segment save(Segment segment) {
        return jdbcAggregateTemplate.save(segment);
    }

    @Override
    public Page<Segment> findSegments(Pageable pageable, String searchTerm) {
        Query countQuery = Query.query(buildCriteria(searchTerm));
        var count = this.jdbcAggregateTemplate.count(countQuery, Segment.class);
        if (count == 0) {
            return Page.empty(pageable);
        }
        Query idsQuery = Query.query(buildCriteria(searchTerm)).with(pageable).columns(PropertyPath.of(Segment::getId).getSegment());
        var orderedIds = this.jdbcAggregateTemplate.findAll(idsQuery, Segment.class).stream().map(Entity::getId).toList();
        Map<Object, Segment> byId = jdbcAggregateTemplate.findAllById(orderedIds, Segment.class).stream()
                .collect(Collectors.toMap(Segment::getId, s -> s));

        List<Segment> segments = orderedIds.stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .toList();
        return new PageImpl<>(segments, pageable, count);

    }

    @Override
    public Optional<Segment> findByID(SegmentID segmentID) {
        return Optional.ofNullable(jdbcAggregateTemplate.findById(segmentID, Segment.class));
    }

    private Criteria buildCriteria(String searchTerm) {
        if (!StringUtils.hasText(searchTerm)) {
            return Criteria.empty();
        }
        return Criteria.where(Segment::getName).like("%" + searchTerm + "%").ignoreCase(true)
                .or(Criteria.where(Segment::getDescription).like("%" + searchTerm + "%").ignoreCase(true));

    }
}
