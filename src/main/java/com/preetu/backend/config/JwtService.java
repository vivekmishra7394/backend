package com.preetu.backend.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey secretKey;

	public JwtService(@Value("${jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public String generateToken(Long id, String email) {
		Date now = new Date();

		Date expiry = new Date(now.getTime() + 1000L * 60 * 30);

		return Jwts.builder()
				.subject(email)
				.claim("id", id)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}
}
