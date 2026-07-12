package com.ogoma.marketing.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class JdbcClientConfig {
    @Bean
    public JdbcClient jdbcClient(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                 ConversionService conversionService) {
        // Create JdbcClient with the configured ConversionService
        return JdbcClient.create(namedParameterJdbcTemplate, conversionService);
    }
}
