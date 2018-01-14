package com.licraft.apt.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigValue {

    String path() default "";

    String defaultsTo() default "";

    char colorChar() default ' ';

    boolean valueKey() default false;

}
