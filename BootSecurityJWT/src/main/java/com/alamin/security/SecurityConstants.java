package com.alamin.security;

public class SecurityConstants {
	
	public static final long JWT_EXPIRATION = 70000;
	public static final String JWT_SECRET_STRING = "your-secure-32-characters-or-more-secret-key";
	public static final byte[] JWT_SECRET = JWT_SECRET_STRING.getBytes();

}
