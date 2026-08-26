package com.ogoma.marketing.campaignsbatchprocessor;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = "com.ogoma.marketing")
@EntityScan(basePackages = "com.ogoma.marketing")
@ComponentScan(basePackages = "com.ogoma.marketing")
public class CompositionRoot {
}
