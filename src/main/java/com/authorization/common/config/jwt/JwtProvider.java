package com.authorization.common.config.jwt;


import com.authorization.common.config.properties.JwtProperties;
import com.authorization.util.DateConvertor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public LocalDateTime extractExpiration(String token) {
        return DateConvertor.toLocalDateTime(extractClaim(token, Claims::getExpiration));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now());
    }

    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, username, jwtProperties.getSecretKey(), jwtProperties.getAccessTokenExpired());
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, username, jwtProperties.getSecretKey(), jwtProperties.getRefreshTokenExpired());
    }

    private String generateToken(Map<String, Object> claims, String subject, String key, Long expiryTime) {

        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(expiryTime);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(DateConvertor.toDate(LocalDateTime.now()))
                .setExpiration(DateConvertor.toDate(expiryDate))
                .signWith(jwtProperties.getSignatureAlgorithm(), key)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    public Long getAccessTokenExpirationDate() {
        return jwtProperties.getAccessTokenExpired();
    }

    public Long getRefreshTokenExpirationDate() {
        return jwtProperties.getRefreshTokenExpired();
    }
}