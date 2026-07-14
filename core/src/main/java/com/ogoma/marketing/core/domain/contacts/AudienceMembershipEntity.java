package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.domain.audience.AudienceId;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;


@Table(name = "audience_membership")
@Getter
public class AudienceMembershipEntity {
    @Id
    private AudienceMembershipID id;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL, prefix = "contact_")
    private ContactID contactID;
    @Version
    private Long version;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL, prefix = "audience_")
    private AudienceId audienceId;
    private Instant joinedAt;

    private AudienceMembershipEntity() {
        this.id = new AudienceMembershipID();
        this.joinedAt = Instant.now();
    }

    private AudienceMembershipEntity(ContactID contactID, AudienceId audienceId) {
        this();
        this.contactID = contactID;
        this.audienceId = audienceId;
    }

    public static AudienceMembershipEntity join(ContactID contactId, AudienceId audienceId) {
        return new AudienceMembershipEntity(contactId, audienceId);
    }
}
