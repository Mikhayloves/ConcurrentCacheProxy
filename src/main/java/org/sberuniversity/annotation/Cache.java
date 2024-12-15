package org.sberuniversity.annotation;

import org.sberuniversity.pathcache.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {
    CacheType cache() default CacheType.IN_MEMORY; //по умолчанию в памяти

    String fileCacheName() default "";

    boolean zip() default false; // нужно ли архивировать

    int valuableArgCount() default CacheConsts.INCLUDE_ALL_ARGS;

    String fileCachePath() default "";

}
