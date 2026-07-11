package com.ogoma.marketing.api.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

@Configuration
public class SecurityConfig {
    @Bean
    public Customizer<ServerHttpSecurity> swaggerPathPermitAllCustomizer() {
        return (http -> {
            String[] excludedPaths = {
                    "/v3/api-docs/**",    // OpenAPI v3 JSON/YAML definitions
                    "/swagger-ui/**",     // Swagger UI HTML, JS, and CSS files
                    "/swagger-ui.html"};
            http.authorizeExchange(
                    authorize -> authorize
                            .pathMatchers(excludedPaths).permitAll()
            );
        });
    }
}
