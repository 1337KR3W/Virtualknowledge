package com.privatebay.virtualknowledge.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expirationTime;

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String generateToken(String email, Long userId, Collection<? extends GrantedAuthority> authorities) {
		List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

		return Jwts.builder().setSubject(email).claim("userId", userId).claim("roles", roles).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	public List<String> extractRoles(String token) {
		Claims claims = extractAllClaims(token);
		Object roles = claims.get("roles");
		if (roles instanceof List<?>) {
			return ((List<?>) roles).stream().map(Object::toString).collect(Collectors.toList());
		}
		return List.of();
	}

	public String extractEmail(String token) {
		return extractAllClaims(token).getSubject();
	}

	public Long extractUserId(String token) {
		Object userId = extractAllClaims(token).get("userId");
		return (userId instanceof Number) ? ((Number) userId).longValue() : null;
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}
}