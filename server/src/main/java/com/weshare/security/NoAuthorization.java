package com.weshare.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this when a method doesn't need authorization.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoAuthorization {
}
