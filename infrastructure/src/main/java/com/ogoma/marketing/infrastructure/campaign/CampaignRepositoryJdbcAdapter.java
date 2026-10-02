package com.ogoma.marketing.infrastructure.campaign;

import com.ogoma.marketing.core.application.campaign.queries.GetCampaignByIDView;
import com.ogoma.marketing.core.application.campaign.queries.TargetAudience;
import com.ogoma.marketing.core.application.campaign.queries.TargetSegment;
import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.domain.campaigns.Channel;
import com.ogoma.marketing.core.domain.outbox.DomainEventToOutboxConverter;
import com.ogoma.marketing.infrastructure.jooq.tables.*;
import org.jooq.Converter;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.EnumConverter;
import org.springframework.data.core.PropertyPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.multiset;


@Repository
public record CampaignRepositoryJdbcAdapter(
        JdbcAggregateTemplate jdbcAggregateTemplate,

        DSLContext dslContext,
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
    public Optional<GetCampaignByIDView> findDetailsByID(CampaignID campaignID) {
        Converter<String, Channel> converter = new EnumConverter<>(String.class, Channel.class);

        return dslContext.select(
                        Campaigns.CAMPAIGNS.ID,
                        Campaigns.CAMPAIGNS.NAME,
                        Campaigns.CAMPAIGNS.DESCRIPTION,
                        Campaigns.CAMPAIGNS.STATUS,
                        multiset(
                                dslContext.select(
                                                Audiences.AUDIENCES.ID,
                                                Audiences.AUDIENCES.NAME
                                        ).from(Audiences.AUDIENCES).join(
                                                CampaignAudience.CAMPAIGN_AUDIENCE
                                        ).on(Audiences.AUDIENCES.ID.eq(CampaignAudience.CAMPAIGN_AUDIENCE.AUDIENCE_ID))
                                        .where(Campaigns.CAMPAIGNS.ID.eq(CampaignAudience.CAMPAIGN_AUDIENCE.CAMPAIGN_ID))
                        )
                                .convertFrom(result ->
                                        result.map(r -> new TargetAudience(r.get(Audiences.AUDIENCES.ID), r.get(Audiences.AUDIENCES.NAME)))
                                )

                                .as("target_audiences"),
                        multiset(
                                dslContext.select(
                                                Segments.SEGMENTS.ID,
                                                Segments.SEGMENTS.NAME
                                        ).from(Segments.SEGMENTS).join(CampaignSegments.CAMPAIGN_SEGMENTS)
                                        .on(Segments.SEGMENTS.ID.eq(CampaignSegments.CAMPAIGN_SEGMENTS.SEGMENT_ID))
                                        .where(CampaignSegments.CAMPAIGN_SEGMENTS.CAMPAIGN_ID.eq(Campaigns.CAMPAIGNS.ID)))
                                .convertFrom(result ->
                                        result.map(r ->
                                                new TargetSegment(
                                                        r.get( Segments.SEGMENTS.ID),
                                                        r.get(Segments.SEGMENTS.NAME)
                                                )
                                        )
                                )
                                .as("target_segments"),
                        // 1. Correct multiset structure with type-safe conversion
                        multiset(
                                dslContext.select(CampaignChannels.CAMPAIGN_CHANNELS.CHANNEL.convert( converter))
                                        .from(CampaignChannels.CAMPAIGN_CHANNELS)
                                        // The correlation here is enough; no need to filter by campaignID twice
                                        .where(CampaignChannels.CAMPAIGN_CHANNELS.CAMPAIGN_ID.eq(Campaigns.CAMPAIGNS.ID))
                        )
                                .convertFrom(result->result.map(Record1::value1))
                                .as("channels")
                ).from(Campaigns.CAMPAIGNS)
                .where(Campaigns.CAMPAIGNS.ID.eq(campaignID.id()))
                .fetchOptional(r ->
                new GetCampaignByIDView(
                        r.get(Campaigns.CAMPAIGNS.ID),
                        r.get(Campaigns.CAMPAIGNS.NAME),
                        r.get(Campaigns.CAMPAIGNS.DESCRIPTION),
                        r.get(Campaigns.CAMPAIGNS.STATUS),
                        r.value7(),
                        r.value6(),
                        r.value5()
                )
        );
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
