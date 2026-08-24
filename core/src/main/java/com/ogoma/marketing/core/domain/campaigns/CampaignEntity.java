package com.ogoma.marketing.core.domain.campaigns;


import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.sharedkernel.AggregateRoot;
import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "campaigns")
@Getter
public class CampaignEntity extends AggregateRoot<CampaignID> {
    private String name;
    private String description;
    private String createdBy;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    private String lastUpdatedBy;
    private Status status;
    @Embedded.Nullable()
    private CampaignConfiguration campaignConfiguration;
    @MappedCollection(idColumn = "campaign_id")
    private Set<CampaignAudienceRef> audienceRefs = new HashSet<>();


    private CampaignEntity() {
        super(null);
    }

    private CampaignEntity(
            String name,
            String description,
            Set<AudienceId> audienceIds,
            CampaignConfiguration configuration,
            String createdBy, Instant createdAt) {
        CustomAssert.hasLength(name, () -> new IllegalArgumentException("Name is required"));
        CustomAssert.notNull(configuration, () -> new IllegalArgumentException("Configuration must not be null"));
        CustomAssert.hasLength(createdBy, () -> new IllegalArgumentException("Created by is required"));
        CustomAssert.notNull(createdAt, () -> new IllegalArgumentException("Created at is required"));
        super(new CampaignID());
        this.name = name;
        this.description = description;
        this.audienceRefs.addAll(toAudienceRefs(audienceIds));
        this.campaignConfiguration = configuration;
        this.createdAt = createdAt;
        this.lastUpdatedAt = createdAt;
        this.createdBy = createdBy;
        this.status = Status.DRAFT;
        this.lastUpdatedBy = createdBy;
    }

    public static CampaignEntity createNew(
            String name,
            String description,
            Set<AudienceId> audienceIds,
            CampaignConfiguration configuration,
            String createdBy,
            Clock clock
    ) {
        return new CampaignEntity(name, description, audienceIds, configuration, createdBy, clock.instant());
    }

    public void update(
            String name,
            String description,
            Set<AudienceId> audienceIds,
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
        this.audienceRefs.clear();
        this.audienceRefs.addAll(toAudienceRefs(audienceIds));
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

    public Set<CampaignAudienceRef> getAudienceRefs() {
        return Collections.unmodifiableSet(this.audienceRefs);
    }

    private static Set<CampaignAudienceRef> toAudienceRefs(Set<AudienceId> ids) {
        if (ids == null) {
            return Collections.emptySet();
        }
        return ids.stream()
                .map(CampaignAudienceRef::new)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private void requireDraft() {
        if (status != Status.DRAFT) {
            throw new IllegalStateException(
                    "Only DRAFT campaigns can be updated");
        }
    }
}
