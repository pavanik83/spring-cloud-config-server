package com.wdpr.ee.service.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
// @Target(ElementType.TYPE) //on class level
public @interface AuthZ
{

    public enum Priority
    {
        LOW, MEDIUM, HIGH
    }

    Priority priority() default Priority.MEDIUM;

    String[] tags() default "";

    String createdBy() default "Nixon";

    String lastModified() default "03/01/2014";

}