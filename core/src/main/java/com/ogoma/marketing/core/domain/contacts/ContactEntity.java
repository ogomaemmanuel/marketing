package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "contacts")
@Getter
public class ContactEntity {
    @Id
    private ContactID id;
    @Version
    private Long version;
    private String firstName;
    private String lastName;
    private String email;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    @MappedCollection(idColumn = "contact_id")
    Set<ContactAttributeValue> attributes=new HashSet<>();
    private String lastUpdatedBy;
    private String createdBy;

    private ContactEntity() {
        var now = Instant.now();
        this.id = new ContactID();
        this.version = null;
        this.createdAt = now;
        this.lastUpdatedAt = now;
    }

    private ContactEntity(
            String firstName,
            String lastName,
            String email,
            Map<String, String> attributes,
            String createdBy
    ) {
        CustomAssert.hasText(firstName, () -> new IllegalArgumentException("First name is required"));
        CustomAssert.hasText(lastName, () -> new IllegalArgumentException("Last name is required"));
        CustomAssert.hasText(email, () -> new IllegalArgumentException("Email is required"));
        CustomAssert.hasText(createdBy, () -> new IllegalArgumentException("Created by is required"));
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.attributes =mapAttributeMapToContactAttributte(attributes);
        this.createdBy = createdBy;
        this.lastUpdatedBy = createdBy;
    }

    public static ContactEntity createNew(
            String firstName,
            String lastName,
            String email,
            Map<String, String> attributes,
            String createdBy
    ) {
        return new ContactEntity(firstName, lastName, email, attributes, createdBy);
    }

    public void update(
            String firstName,
            String lastName,
            String email,
            Map<String, String> attributes,
            String lastUpdatedBy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.attributes = mapAttributeMapToContactAttributte(attributes);
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedAt = Instant.now();
    }

    private Set<ContactAttributeValue> mapAttributeMapToContactAttributte(Map<String, String> attributes) {
        return attributes.entrySet().stream().map(entry -> new ContactAttributeValue(entry.getKey(), entry.getValue())).collect(Collectors.toSet());
    }


}
