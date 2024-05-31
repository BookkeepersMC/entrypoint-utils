package com.bookkeepersmc.entrypoint.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface EventLoader {
    Priority priority() default Priority.NORMAL;
    boolean cancelled() default false;
}
