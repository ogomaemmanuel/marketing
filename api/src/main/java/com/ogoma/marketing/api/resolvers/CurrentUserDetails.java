package com.ogoma.marketing.api.resolvers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.oauth2.jwt.Jwt;

@Hidden
public record CurrentUserDetails(
        String email,
        String firstName,
        String lastName,
        String externalId) {

    public CurrentUserDetails(Jwt jwt) {
        this(
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name"),
                jwt.getClaimAsString("sub")
        );
    }
}
