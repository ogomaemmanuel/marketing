package com.ogoma.marketing.core.domain.campaigns;


import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.campaigns.events.CampaignCreatedEvent;
import com.ogoma.marketing.core.domain.campaigns.events.CampaignSentEvent;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.segments.SegmentID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Clock;
import java.time.Instant;
import java.util.*;
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
    Set<CampaignChannel> channels = new HashSet<>();

    @MappedCollection(idColumn = "campaign_id")
    @Getter(AccessLevel.PACKAGE)
    private  Set<CampaignAudience> audiences = new HashSet<>();
    @MappedCollection(idColumn = "campaign_id")
    @Getter(AccessLevel.PACKAGE)
    private  Set<CampaignSegment> segments = new HashSet<>();

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
        this.name = name.trim();
        this.description = description;
        this.audiences.addAll(toAudienceRefs(audienceIds));
        this.segments.addAll(toSegmentRefs(segmentIDS));
        this.campaignConfiguration = configuration;
        this.createdAt = createdAt;
        this.lastUpdatedAt = createdAt;
        this.createdBy = createdBy;
        this.status = Status.DRAFT;
        this.lastUpdatedBy = createdBy;
        this.channels.addAll(syncChannels(configuration));

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
        this.channels.addAll(syncChannels(configuration));
        this.touch(updatedBy, clock.instant());
    }


    public void startSending(String sentBy, Clock clock) {
        if (status != Status.DRAFT) {
            throw new IllegalStateException(
                    "Campaign can only start sending from DRAFT");
        }
        this.status = Status.SENDING;
        this.touch(sentBy, clock.instant());
        this.raiseEvent(new CampaignSentEvent(
                this.getId().id(),
                this.getName(),
                this.getDescription(),
                this.lastUpdatedAt,
                this.lastUpdatedBy
        ));

    }

    private void touch(String lastUpdatedBy, Instant updatedAt) {
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedAt = updatedAt;
    }

    public Set<AudienceId> getAudienceRefs() {
        return Optional.ofNullable( getAudiences()).orElse(Set.of()).stream().map(CampaignAudience::audienceId).collect(Collectors.toSet());
    }

    private static Set<CampaignAudience> toAudienceRefs(Set<AudienceId> ids) {
        return Optional.ofNullable(ids).orElse(Set.of()).stream()
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
        return Optional.ofNullable(this.channels).orElse(Set.of()).stream().map(CampaignChannel::channel).collect(Collectors.toSet());
    }

    private Collection<CampaignSegment> toSegmentRefs(Set<SegmentID> segmentIDS) {

        return Optional.ofNullable(segmentIDS).orElse(Set.of()).stream().map(CampaignSegment::new).collect(Collectors.toSet());
    }

    public Set<SegmentID> getSegmentRefs() {
        return Optional.ofNullable(this.getSegments()).orElse(Set.of()).stream().map(CampaignSegment::segmentId).collect(Collectors.toSet());
    }

    public SmsTemplateID getSmsTemplateId() {
        return campaignConfiguration.smsTemplateID();
    }

    public EmailTemplateID getEmailTemplateId() {
        return campaignConfiguration.emailTemplateID();
    }

    private Set<CampaignChannel> syncChannels(CampaignConfiguration configuration){
        channels.clear();
       return Optional.ofNullable(configuration.channels()).orElse(Set.of()).stream().map(CampaignChannel::new).collect(Collectors.toSet());
    }

}
