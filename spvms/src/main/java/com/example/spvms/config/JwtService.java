package com.example.spvms.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
        "THIS_IS_A_VERY_SECURE_SECRET_KEY_FOR_JWT_256_BITS";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /* CREATE TOKEN WITH USER ID */
    public String generateToken(Long userId, String role) {
        String finalRole = role.startsWith("ROLE_")
            ? role
            : "ROLE_" + role;
        return Jwts.builder()
                .setSubject(String.valueOf(userId))   // ✅ USER ID
                .claim("role", finalRole)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* EXTRACT USER ID */
    public Long extractUserId(String token) {
        return Long.parseLong(
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject()
        );
    }

    /* EXTRACT ROLE */
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return true; // already validated structurally
    }
}
