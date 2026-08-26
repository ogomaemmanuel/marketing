package com.ogoma.marketing.core.domain.contacts;

import org.springframework.data.relational.core.mapping.Table;

@Table("contact_attribute_values")
public record ContactAttributeValue(
        String attribute,
        String value
) {
}
