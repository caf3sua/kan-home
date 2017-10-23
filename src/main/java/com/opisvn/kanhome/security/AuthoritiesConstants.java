package com.opisvn.kanhome.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";
    
    public static final String TECHNICAL = "ROLE_TECHNICAL";
    
    public static final String SUPPORTOR = "ROLE_SUPPORTOR";
    
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
