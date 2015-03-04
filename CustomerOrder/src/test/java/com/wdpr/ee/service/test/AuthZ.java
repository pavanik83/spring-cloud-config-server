package com.wdpr.ee.service.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** AuthZ test */
@Retention(RetentionPolicy.RUNTIME)
// @Target(ElementType.TYPE) //on class level
public @interface AuthZ
{
    /**  */
    public enum Priority
    {
        /** */
        LOW,
        /** */
        MEDIUM,
        /** */
        HIGH
    }

    /** Priority.MEDIUM */
    Priority priority() default Priority.MEDIUM;

    /** "" */
    String[] tags() default "";

    /** "Nixon" */
    String createdBy() default "Nixon";

    /** "03/01/2014" */
    String lastModified() default "03/01/2014";
}
