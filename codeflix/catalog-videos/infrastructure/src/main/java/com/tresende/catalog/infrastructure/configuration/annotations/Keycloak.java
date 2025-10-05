package com.tresende.catalog.infrastructure.configuration.annotations;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Qualifier("Keycloak")
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, PARAMETER, METHOD, TYPE, ANNOTATION_TYPE})
public @interface Keycloak {
}
