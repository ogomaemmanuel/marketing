package com.ogoma.marketing.core.domain.sms;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.time.Instant;


@Table("sms_templates")
@Getter
public class SmsTemplateEntity {
    @Id
    private SmsTemplateID id;
    private String name;
    private String content;
    private String description;
    @Version
    private Long version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;


    private SmsTemplateEntity() {
        id = new SmsTemplateID();
        version = null;
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;

    }

    private SmsTemplateEntity(
            String name,
            String description,
            String content, String createdBy) {
        Assert.hasText(name, "Name is required");
        Assert.hasText(content, "Content is required");
        Assert.hasText(createdBy, "Creator is required");
        this();
        this.name = name;
        this.content = content;
        this.description = description;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }

    public static SmsTemplateEntity createNew(String name,
                                              String description,
                                              String content,
                                              String createdBy) {
        return new SmsTemplateEntity(name, description, content, createdBy);
    }

    public SmsTemplateEntity duplicate(String suggestedName, String actor) {
        var newName = StringUtils.hasText(suggestedName) ? suggestedName : this.name + " Copy";
        return new SmsTemplateEntity(newName, this.description, this.content, actor);
    }



    public void update(
            String name,
            String description,
            String content,
            String actor) {
        Assert.hasText(name, "Name is required");
        Assert.hasText(content, "Content is required");
        Assert.hasText(actor, "Actor is required");
        this.name = name;
        this.description = description;
        this.content = content;
        this.updatedBy = actor;
        this.updatedAt = Instant.now();
    }


}
