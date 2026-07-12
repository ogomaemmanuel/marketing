package com.ogoma.marketing.api.annotations;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.annotation.CurrentSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@CurrentSecurityContext(expression = "authentication.name")
@Retention(RetentionPolicy.RUNTIME)
@Hidden
public @interface CurrentUser {
}
