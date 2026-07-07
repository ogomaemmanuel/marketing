package com.ogoma.marketing.core.domain.email;

import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import java.time.Instant;

@Table(value = "email_templates")
@Getter
public class EmailTemplateEntity {
    @Id
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private EmailTemplateID id;
    @Version
    private Long version;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String lastUpdatedBy;
    private EmailTemplate emailTemplate;

    private EmailTemplateEntity() {
        var now = Instant.now();
        id = new EmailTemplateID();
        version = null;
        createdAt = now;
        updatedAt = now;
    }

    private EmailTemplateEntity(String name, String createdBy, EmailTemplate emailTemplate) {
        Assert.hasText(name, "Template name is require");
        Assert.hasText(createdBy, "Template creator  is required");
        Assert.notNull(emailTemplate, "Email Template is required");
        this();
        this.name = name;
        this.createdBy = createdBy;
        this.lastUpdatedBy = createdBy;
        this.emailTemplate = emailTemplate;
    }

    public static EmailTemplateEntity createNew(String name,
                                                String createdBy,
                                                EmailTemplate emailTemplate) {
        return new EmailTemplateEntity(name, createdBy, emailTemplate);
    }

    public void updateDetails(String name, String actor, EmailTemplate emailTemplate) {
        Assert.hasText(name, "Template name is required");
        Assert.notNull(name, "Email Template is required");
        this.name = name;
        this.updatedAt = Instant.now();
        this.emailTemplate = emailTemplate;
        this.lastUpdatedBy = actor;
    }

    public EmailTemplateEntity clone(
            String suggestedName,
            String actor) {
        suggestedName = suggestedName != null ? suggestedName : this.name + " Copy";
        return new EmailTemplateEntity(
                suggestedName,
                actor,
                this.getEmailTemplate());
    }
}
