package com.ogoma.marketing.api.resolvers;

import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CurrentUserDetails.class);
    }

    @Override
    public Mono<Object> resolveArgument(@NonNull MethodParameter parameter, @NonNull BindingContext bindingContext, ServerWebExchange exchange) {
        return exchange
                .getPrincipal()
                .cast(org.springframework.security.core.Authentication.class)
                .mapNotNull(authentication -> (Jwt) authentication.getPrincipal())
                .map(CurrentUserDetails::new)
                .cast(Object.class);
    }
}
