package com.privatebay.virtualknowledge.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

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

	// NUEVO: Método para extraer los roles de forma segura
	public List<String> extractRoles(String token) {
		Claims claims = extractAllClaims(token);
		Object roles = claims.get("roles");

		if (roles instanceof List<?>) {
			return ((List<?>) roles).stream().map(Object::toString).collect(Collectors.toList());
		}
		return List.of(); // Devuelve lista vacía si no hay roles
	}

	public String extractEmail(String token) {
		return extractAllClaims(token).getSubject();
	}

	public Long extractUserId(String token) {
		// Usamos Number para evitar errores de casteo entre Integer/Long según la
		// librería
		Object userId = extractAllClaims(token).get("userId");
		if (userId instanceof Number) {
			return ((Number) userId).longValue();
		}
		return null;
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}
}
