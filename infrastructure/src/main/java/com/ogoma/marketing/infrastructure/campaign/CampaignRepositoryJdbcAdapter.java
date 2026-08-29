package com.ogoma.marketing.infrastructure.campaign;

import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.domain.outbox.DomainEventToOutboxConverter;
import org.springframework.data.core.PropertyPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public record CampaignRepositoryJdbcAdapter(
        JdbcAggregateTemplate jdbcAggregateTemplate,
        Clock clock

) implements CampaignRepository {

    @Override
    public CampaignEntity save(CampaignEntity campaignEntity) {
        var outbox = campaignEntity.pullDomainEvents().stream().map(x -> DomainEventToOutboxConverter.convert(x, clock)).collect(Collectors.toSet());
        jdbcAggregateTemplate.saveAll(outbox);
        return jdbcAggregateTemplate.save(campaignEntity);

    }

    @Override
    public Optional<CampaignEntity> findByID(CampaignID campaignID) {
        return Optional.ofNullable(jdbcAggregateTemplate.findById(campaignID, CampaignEntity.class));
    }

    @Override
    public Page<CampaignEntity> findAllBy(String searchTerm, Pageable pageable) {
        Criteria criteria = Criteria.empty();
        if (StringUtils.hasText(searchTerm)) {
            criteria = Criteria.where(PropertyPath.of(CampaignEntity::getName))
                    .like("%" + searchTerm.trim() + "%").or(
                            Criteria.where(PropertyPath.of(CampaignEntity::getDescription)).like("%" + searchTerm.trim() + "%")
                    );
        }
        var countQuery = Query.query(criteria);
        var count = jdbcAggregateTemplate.count(countQuery, CampaignEntity.class);
        if (count == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        var dataQuery = Query.query(criteria).with(pageable);
        var data = jdbcAggregateTemplate.findAll(dataQuery, CampaignEntity.class);
        return new PageImpl<>(data, pageable, count);
    }
}
