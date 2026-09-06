package com.ogoma.marketing.core.domain.campaigns;


import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.campaigns.events.CampaignCreatedEvent;
import com.ogoma.marketing.core.domain.segments.SegmentID;
import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "campaigns")

public class CampaignEntity extends AggregateRoot<CampaignID> {
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private String createdBy;
    @Getter
    private Instant createdAt;
    @Getter
    private Instant lastUpdatedAt;
    @Getter
    private String lastUpdatedBy;
    @Getter
    private Status status;
    @Embedded.Nullable()
    private CampaignConfiguration campaignConfiguration;
    @MappedCollection(idColumn = "campaign_id")
    @Getter(AccessLevel.PACKAGE)
    Set<CampaignChannel> channels = new HashSet<>();

    @MappedCollection(idColumn = "campaign_id")
    @Getter(AccessLevel.PACKAGE)
    private Set<CampaignAudience> audiences = new HashSet<>();

    @MappedCollection(idColumn = "campaign_id")
    @Getter(AccessLevel.PACKAGE)
    private Set<CampaignSegment> segments = new HashSet<>();

    private CampaignEntity() {
        super(null);
    }

    private CampaignEntity(
            String name,
            String description,
            Set<AudienceId> audienceIds,
            Set<SegmentID> segmentIDS,
            CampaignConfiguration configuration,
            String createdBy, Instant createdAt) {
        CustomAssert.hasLength(name, () -> new IllegalArgumentException("Name is required"));
        CustomAssert.notNull(configuration, () -> new IllegalArgumentException("Configuration must not be null"));
        CustomAssert.hasLength(createdBy, () -> new IllegalArgumentException("Created by is required"));
        CustomAssert.notNull(createdAt, () -> new IllegalArgumentException("Created at is required"));
        super(new CampaignID());
        this.name = name;
        this.description = description;
        this.audiences.addAll(toAudienceRefs(audienceIds));
        this.segments.addAll(toSegmentRefs(segmentIDS));
        this.campaignConfiguration = configuration;
        this.createdAt = createdAt;
        this.lastUpdatedAt = createdAt;
        this.createdBy = createdBy;
        this.status = Status.DRAFT;
        this.lastUpdatedBy = createdBy;
        this.channels = configuration.channels().stream().map(CampaignChannel::new).collect(Collectors.toSet());

    }


    public static CampaignEntity createNew(
            String name,
            String description,
            Set<AudienceId> audienceIds,
            Set<SegmentID> segmentIDS,
            CampaignConfiguration configuration,
            String createdBy,
            Clock clock
    ) {

        var campaign = new CampaignEntity(
                name,
                description,
                audienceIds,
                segmentIDS,
                configuration,
                createdBy,
                clock.instant());
        campaign.raiseEvent(new CampaignCreatedEvent(
                campaign.getId().id(),
                campaign.getName(),
                campaign.getDescription(),
                campaign.getCreatedAt(),
                campaign.getCreatedBy()
        ));
        return campaign;
    }

    public void update(
            String name,
            String description,
            Set<AudienceId> audienceIds,
            Set<SegmentID> segmentIDS,
            CampaignConfiguration configuration,
            String updatedBy, Clock clock) {
        CustomAssert.hasLength(name, () -> new IllegalArgumentException("Name is required"));
        CustomAssert.notNull(configuration, () -> new IllegalArgumentException("Configuration must not be null"));
        CustomAssert.hasLength(updatedBy, () -> new IllegalArgumentException("Updated by is required"));
        CustomAssert.notNull(clock, () -> new IllegalArgumentException("Clock cannot be null"));
        requireDraft();
        this.name = name;
        this.description = description;
        this.campaignConfiguration = configuration;
        this.audiences.clear();
        this.audiences.addAll(toAudienceRefs(audienceIds));
        this.segments.clear();
        this.segments.addAll(toSegmentRefs(segmentIDS));
        this.touch(updatedBy, clock.instant());
    }


    public void startSending(String sentBy, Clock clock) {
        if (status != Status.DRAFT) {
            throw new IllegalStateException(
                    "Campaign can only start sending from DRAFT");
        }
        this.status = Status.SENDING;
        this.touch(sentBy, clock.instant());
    }

    private void touch(String lastUpdatedBy, Instant updatedAt) {
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedAt = updatedAt;
    }

    public Set<AudienceId> getAudienceRefs() {
        return getAudiences().stream().map(CampaignAudience::audienceId).collect(Collectors.toSet());
    }

    private static Set<CampaignAudience> toAudienceRefs(Set<AudienceId> ids) {
        if (ids == null) {
            return Collections.emptySet();
        }
        return ids.stream()
                .map(CampaignAudience::new)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private void requireDraft() {
        if (status != Status.DRAFT) {
            throw new IllegalStateException(
                    "Only DRAFT campaigns can be updated");
        }
    }

    public Set<Channel> getChannels() {
        return this.channels.stream().map(CampaignChannel::channel).collect(Collectors.toSet());
    }

    private Collection<CampaignSegment> toSegmentRefs(Set<SegmentID> segmentIDS) {
        if (segmentIDS == null) {
            return Set.of();
        }
        return segmentIDS.stream().map(CampaignSegment::new).collect(Collectors.toSet());
    }

}
