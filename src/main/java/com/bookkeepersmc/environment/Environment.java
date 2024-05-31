package com.bookkeepersmc.environment;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, METHOD, CONSTRUCTOR, PACKAGE, ANNOTATION_TYPE})
@Repeatable(Environments.class)
public @interface Environment {
    public EnvironmentType value();
    public Class<?> _interface() default Object.class;
}
