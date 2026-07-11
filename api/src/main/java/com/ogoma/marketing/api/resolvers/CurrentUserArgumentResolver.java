package com.ogoma.marketing.api.resolvers;

import org.jspecify.annotations.NullMarked;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public record CurrentUserArgumentResolver() implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CurrentUserDetails.class);
    }

    @Override
    @NullMarked
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        return exchange
                .getPrincipal()
                .cast(org.springframework.security.core.Authentication.class)
                .mapNotNull(authentication -> (Jwt) authentication.getPrincipal())
                .map(CurrentUserDetails::new)
                .cast(Object.class);
    }
}
