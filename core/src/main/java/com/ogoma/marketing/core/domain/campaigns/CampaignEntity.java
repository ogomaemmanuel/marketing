package com.ogoma.marketing.core.domain.campaigns;


import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import com.ogoma.marketing.core.sharedkernel.AggregateRoot;
import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import lombok.Getter;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
    private UUID emailTemplateID;
    private Set<Channel> channels;

    private UUID smsTemplateID;


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
            CampaignConfiguration configuration,
            String createdBy) {
        CustomAssert.hasLength(name, () -> new IllegalArgumentException("Name is required"));
        CustomAssert.notNull(configuration, () -> new IllegalArgumentException("Configuration must not be null"));
        CustomAssert.hasLength(createdBy, () -> new IllegalArgumentException("Created by is required"));
        this();
        this.name = name;
        this.description = description;
        this.emailTemplateID =
                Optional.ofNullable(configuration.emailTemplateID()).map(EmailTemplateID::id).orElse(null);
        this.smsTemplateID =
                Optional.ofNullable( configuration.smsTemplateID()).map(SmsTemplateID::id).orElse(null);
        this.channels = configuration.channels();
        this.createdBy = createdBy;

        this.lastUpdatedBy = createdBy;
        this.lastUpdatedAt = Instant.now();
    }

    public static CampaignEntity createNew(
            String name,
            String description,
            CampaignConfiguration configuration,
            String createdBy
    ) {
        return new CampaignEntity(name, description, configuration, createdBy);
    }

    public void update(
            String name,
            String description,
            CampaignConfiguration configuration,
            String updatedBy) {
        CustomAssert.hasLength(name, () -> new IllegalArgumentException("Name is required"));
        CustomAssert.notNull(configuration, () -> new IllegalArgumentException("Configuration must not be null"));
        CustomAssert.hasLength(updatedBy, () -> new IllegalArgumentException("Created by is required"));
        this.name = name;
        this.description = description;
        this.emailTemplateID =
                Optional.ofNullable(configuration.emailTemplateID()).map(EmailTemplateID::id).orElse(null);
        this.smsTemplateID =
               Optional.ofNullable( configuration.smsTemplateID()).map(SmsTemplateID::id).orElse(null);
        this.channels = configuration.channels();
        this.touch(updatedBy);
    }

    private void touch(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedAt = Instant.now();
    }


}
