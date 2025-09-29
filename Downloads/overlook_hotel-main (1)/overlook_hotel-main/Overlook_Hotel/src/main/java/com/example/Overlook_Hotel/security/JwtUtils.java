package com.example.Overlook_Hotel.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    // Clé secrète suffisamment longue pour HS512 (au moins 64 caractères)
    @Value("${app.jwt.secret:overlookHotelSecretKeyForJWT2024VerySecureAndLongEnoughForHS512AlgorithmSecurity}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}") // 24 heures en millisecondes
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        // Si la clé est trop courte, génère automatiquement une clé sécurisée
        if (jwtSecret.length() < 64) {
            return Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authentication.getAuthorities().iterator().next().getAuthority());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token validation error: " + e.getMessage());
            return false;
        }
    }
}