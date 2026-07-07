package com.ogoma.marketing.core.domain.users;


import com.ogoma.marketing.core.sharedkernel.EncryptedStringField;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "users")
@Getter
public class UserEntity {
    @Id
    private UserID id;
    @Version
    private Long version;
    private EncryptedStringField firstName;
    private EncryptedStringField lastName;
    private EncryptedStringField email;
    private String externalId;
    private Instant createdAt;
    private Instant updatedAt;

    private UserEntity() {
        this.id = new UserID();
        version = null;
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    private UserEntity(String firstName, String lastName, String email, String externalId) {
        this();
        this.firstName = new EncryptedStringField(firstName);
        this.lastName = new EncryptedStringField(lastName);
        this.email = new EncryptedStringField(email);
        this.externalId = externalId;
    }

    public void update(String firstName, String lastName, String email) {
        this.firstName = new EncryptedStringField(firstName);
        this.lastName = new EncryptedStringField(lastName);
        this.email = new EncryptedStringField(email);
        this.updatedAt = Instant.now();
    }

    public static UserEntity createNew(String firstName, String lastName, String email, String externalId) {
        return new UserEntity(firstName, lastName, email, externalId);
    }
}
