package com.ogoma.marketing.core.domain.campaigns;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.List;

@Table(name = "campaigns")
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

    private List<String> tags;
}
