package com.bookkeepersmc.entrypoint.api;

import com.bookkeepersmc.environment.EnvironmentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Gets initialized in loader
 */
public @interface Mod {
    String value();

    EnvironmentType[] environmentType() default {EnvironmentType.CLIENT, EnvironmentType.SERVER};
}
