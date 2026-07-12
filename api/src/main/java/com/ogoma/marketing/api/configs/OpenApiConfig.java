package com.ogoma.marketing.api.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI(OAuth2ResourceServerProperties resourceServerProperties) {
        final String securitySchemeName = "OidcAuth";
        Assert.notNull(resourceServerProperties.getJwt().getIssuerUri(), "Resource Server Issuer URI is required");
        String cleanIssuerUri = resourceServerProperties.getJwt().getIssuerUri().replaceAll("/$", "");
        String oidcDiscoveryUrl = "%s/.well-known/openid-configuration".formatted(cleanIssuerUri);
        return new OpenAPI()
                .info(new Info()
                        .title("Marketing App")
                        .version("1.0.0")
                        .description("""
                                Marketing application that allows one to create sms and email templates , send transactional emails and sms, manage contacts,
                                Create and manage campaigns
                                """))
                // 1. Apply the security requirement globally to all endpoints
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 2. Define the OIDC Security Scheme using the discovery URL
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OPENIDCONNECT)
                                        .openIdConnectUrl(oidcDiscoveryUrl)

                        ));
    }
}
