package com.alamin.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerator {
	
	private static final SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET);
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
		
		String token = Jwts.builder()
				.claim("sub", username)
				.claim("iat", new Date())
				.claim("exp", expireDate)
				.signWith(key)
				.compact();
		return token;
	}
	
	public String getUsernameFromJWT(String token) {
		
		Claims claims = Jwts.parser()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
			
			return true;
		}catch (Exception e) {
			throw new AuthenticationCredentialsNotFoundException("Jwt was expired or incorrect!");
		}
	}

}
