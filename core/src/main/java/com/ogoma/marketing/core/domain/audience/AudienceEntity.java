package com.ogoma.marketing.core.domain.audience;

import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
    private AudienceType type;
    @Column("ruleset")
    private RuleSet ruleSet;

    private AudienceEntity() {
    }

    private AudienceEntity(String name, String createdBy, AudienceType audienceType) {
        CustomAssert.hasText(name, () -> new IllegalArgumentException("Name is required"));
        CustomAssert.hasText(createdBy, () -> new IllegalArgumentException("Created by is required"));
        CustomAssert.notNull(audienceType, () -> new IllegalArgumentException("Audience type must not be null"));
        this();
        this.id = new AudienceId();
        this.name = name;
        this.type = audienceType;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;

    }

    private AudienceEntity(String name, String createdBy, AudienceType audienceType, RuleSet ruleSet) {
        CustomAssert.notNull(ruleSet, () -> new IllegalArgumentException("RuleSet must not be null"));
        this(name, createdBy, audienceType);
        this.ruleSet = ruleSet;
    }

    public static AudienceEntity createStaticAudience(String name, String createdBy) {
        return new AudienceEntity(name, createdBy, AudienceType.STATIC);
    }

    public void update(String name, String updatedBy) {
        CustomAssert.hasText(name, () -> new IllegalArgumentException("Name is required"));
        CustomAssert.hasText(updatedBy, () -> new IllegalArgumentException("Updated by is required"));
        this.name = name;
        this.updatedBy = updatedBy;
        this.updatedAt = Instant.now();
    }

    public void updateRules(RuleSet ruleSet, String updatedBy) {
        CustomAssert.hasText(updatedBy, () -> new IllegalArgumentException("Updated by is required"));
        CustomAssert.isTrue(this.type == AudienceType.DYNAMIC, () -> new IllegalStateException("Cannot apply rules to non dynamic audience"));
        CustomAssert.notNull(ruleSet, () -> new IllegalArgumentException("RuleSet must not be null"));
        this.ruleSet = ruleSet;
        this.updatedBy = updatedBy;
        this.updatedAt = Instant.now();
    }

    public static AudienceEntity createDynamicAudience(
            String name,
            String createdBy,
            RuleSet ruleSet

    ) {
        return new AudienceEntity(name, createdBy, AudienceType.DYNAMIC, ruleSet);
    }


}
