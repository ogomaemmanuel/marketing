package com.ogoma.marketing.core.domain.contacts;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

@Table(name = "contacts")
@Getter
public class ContactEntity {
    @Id
    private ContactID id;
    @Version
    private Long version;
    private String name;
    Map<String, String> attributes;

}
