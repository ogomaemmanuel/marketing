package com.ogoma.marketing.core.domain.campaigns;


import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.sharedkernel.AggregateRoot;
import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import lombok.Getter;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

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
    @Version
    private Long version;
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
        super(new CampaignID());
        var now = Instant.now();
        this.createdAt = now;
        this.lastUpdatedAt = now;
        status = Status.DRAFT;
        this.version = null;
    }

    private CampaignEntity(
            String name,
            String description,
            Set<AudienceId> audienceIds,
            CampaignConfiguration configuration,
            String createdBy) {
        CustomAssert.hasLength(name, () -> new IllegalArgumentException("Name is required"));
        CustomAssert.notNull(configuration, () -> new IllegalArgumentException("Configuration must not be null"));
        CustomAssert.hasLength(createdBy, () -> new IllegalArgumentException("Created by is required"));
        this();
        this.name = name;
        this.description = description;
        this.audienceRefs.addAll(toAudienceRefs(audienceIds));
        this.campaignConfiguration = configuration;
        this.createdBy = createdBy;

        this.lastUpdatedBy = createdBy;
        this.lastUpdatedAt = Instant.now();
    }

    public static CampaignEntity createNew(
            String name,
            String description,
            Set<AudienceId> audienceIds,
            CampaignConfiguration configuration,
            String createdBy
    ) {
        return new CampaignEntity(name, description, audienceIds, configuration, createdBy);
    }

    public void update(
            String name,
            String description,
            Set<AudienceId> audienceIds,
            CampaignConfiguration configuration,
            String updatedBy) {
        CustomAssert.hasLength(name, () -> new IllegalArgumentException("Name is required"));
        CustomAssert.notNull(configuration, () -> new IllegalArgumentException("Configuration must not be null"));
        CustomAssert.hasLength(updatedBy, () -> new IllegalArgumentException("Updated by is required"));
        this.name = name;
        this.description = description;
        this.campaignConfiguration = configuration;
        this.audienceRefs.clear();
        this.audienceRefs.addAll(toAudienceRefs(audienceIds));
        this.touch(updatedBy);
    }

    private void touch(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedAt = Instant.now();
    }

    public Set<CampaignAudienceRef> getAudienceRefs() {
        return Collections.unmodifiableSet(this.audienceRefs);
    }
    private static Set<CampaignAudienceRef> toAudienceRefs(Set<AudienceId> ids) {
        if (ids == null) {
            return new HashSet<>();
        }
        return ids.stream()
                .map(CampaignAudienceRef::new)
                .collect(Collectors.toCollection(HashSet::new));
    }

}
