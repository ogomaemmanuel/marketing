package com.ogoma.marketing.core.domain.polls.entities;

import org.springframework.data.relational.core.mapping.Table;

@Table("poll_options")
public record Option(String value) {
}
