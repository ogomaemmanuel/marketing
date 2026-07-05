package com.ogoma.marketing.api.annotations;

import org.springframework.security.core.annotation.CurrentSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@CurrentSecurityContext(expression = "authentication.name")
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
