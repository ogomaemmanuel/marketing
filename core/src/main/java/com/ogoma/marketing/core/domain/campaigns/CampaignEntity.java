package com.ogoma.marketing.core.domain.campaigns;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.List;

@Table(name = "campaigns")
@Getter
public class CampaignEntity {
    @Id
    private CampaignID id;

    @Version
    private Long version;

    private String createdBy;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    private String lastUpdatedBy;
    private String name;
    private Stage currentStage;

    private Plan plan;

    private List<String> tags;

    private CampaignEntity() {
        var now = Instant.now();
        this.createdAt = now;
        this.lastUpdatedAt = now;
        this.version = null;
    }

    private CampaignEntity()
}
