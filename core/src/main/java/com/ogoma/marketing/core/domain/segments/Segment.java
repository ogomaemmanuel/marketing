package com.ogoma.marketing.core.domain.segments;

import com.ogoma.marketing.core.domain.audience.RuleSet;
import com.ogoma.marketing.core.sharedkernel.AggregateRoot;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.Instant;

@Getter
@Table("segments")
public class Segment extends AggregateRoot<SegmentID> {
    private String name;
    private String description;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    private String createdBy;
    private String lastUpdatedBy;
    @Column("ruleset")
    private RuleSet ruleSet;

    private Segment() {
        super(null);
    }

    private Segment(String name,
                    String description,
                    RuleSet rules,
                    String createdBy,
                    Instant createdAt) {
        Assert.hasText(name, "Name is required");
        Assert.notNull(rules, "Rules are required");
        Assert.hasText(createdBy, "Created by is required");
        Assert.notNull(createdAt, "Created at is required");
        super(new SegmentID());
        this.name = name;
        this.ruleSet = rules;
        this.description=description;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastUpdatedAt = createdAt;
        this.lastUpdatedBy = createdBy;
    }

    public static Segment createNew(String name,
                                    String description,
                                    RuleSet rules,
                                    String createdBy,
                                    Clock clock) {
        Assert.notNull(clock, "Clock is required");
        return new Segment(name,description, rules, createdBy, clock.instant());
    }

    public void updateRules(RuleSet rules, String updatedBy, Clock clock) {
        Assert.notNull(rules, "Rules are required");
        Assert.hasText(updatedBy, "Updated by is required");
        Assert.notNull(clock, "Clock is required");
        this.ruleSet = rules;
        this.lastUpdatedBy = updatedBy;
        this.lastUpdatedAt = clock.instant();
    }

    public RuleSet.SqlSegment toSql(String prefix) {

        return ruleSet.toNamedSQL(prefix);
    }
}
