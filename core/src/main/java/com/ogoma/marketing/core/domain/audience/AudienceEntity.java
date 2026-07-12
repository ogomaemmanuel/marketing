package com.ogoma.marketing.core.domain.audience;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import java.time.Instant;

/**
 * Audience is used to group contacts,
 * we can then select an audience to when creating a campaign
 *
 *
 */
@Getter
@Table(name = "audiences")
public class AudienceEntity {
    @Id
    private AudienceId id;
    private String name;
    @Version
    private Long version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private AudienceEntity() {
        var now = Instant.now();
        this.id = new AudienceId();
        this.version = null;
        this.createdAt = now;
        this.updatedAt = now;
    }

    private AudienceEntity(String name, String createdBy) {
        Assert.hasText(name, "Name is required");
        Assert.hasText(createdBy, "Updated by is required");
        this();
        this.name = name;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }

    public static AudienceEntity createNew(String name, String createdBy) {
        return new AudienceEntity(name, createdBy);
    }

    public void update(String name, String updatedBy) {
        Assert.hasText(name, "Name is required");
        Assert.hasText(updatedBy, "Updated by is required");
        this.name = name;
        this.updatedBy = updatedBy;
        this.updatedAt = Instant.now();
    }

}
