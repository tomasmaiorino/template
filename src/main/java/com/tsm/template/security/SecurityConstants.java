package com.tsm.template.security;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/api/v1/users/auth";
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
	public static final String SIGNING_KEY = "4uerw@@3err";

}
