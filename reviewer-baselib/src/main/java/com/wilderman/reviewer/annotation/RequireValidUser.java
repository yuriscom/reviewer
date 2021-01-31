package com.wilderman.reviewer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tag a method as requiring a valid user
 * This will ensure that a user object is sourced formt he db and attached tot he request at all times.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireValidUser {}
