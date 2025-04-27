package com.library.library.config;

import com.library.library.model.User;
import com.library.library.service.auth.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private final String secretKey = "f3a53bd31b02b6a27269dc1d0e9835ad6769d0a2e18f0df5e87182f29900140b";
    private final long accessTokenExpiration = 30 * 60 * 1000; // 30 minutes
    private final long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000; // 7 days
    private final TokenBlacklistService tokenBlacklistService;

    public JwtService(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); //Retrieves all claims from JWT
        return claimsResolver.apply(claims);
    }


public String generateAccessToken(UserDetails userDetails) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("tokenType", "ACCESS");

    if (userDetails instanceof User user) {
        extraClaims.put("userId", user.getId().toString());
        extraClaims.put("role", "ROLE_" + user.getRole().name());
    }
    return buildToken(extraClaims, userDetails, accessTokenExpiration);
}

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("tokenType", "REFRESH");

        if (userDetails instanceof User user) {
            extraClaims.put("userId", user.getId().toString());
            extraClaims.put("role", "ROLE_" + user.getRole().name());
        }
        return buildToken(extraClaims, userDetails, refreshTokenExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long accessTokenExpiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        if (tokenBlacklistService.isAccessTokenBlacklisted(token)) {
            return false;
        }

        final String username = extractUsername(token);
        final String tokenType = extractTokenType(token);

        return username.equals(userDetails.getUsername())
                && "ACCESS".equals(tokenType)
                && isTokenValid(token);
    }

    public boolean isRefreshTokenValid(String token) {
        if (tokenBlacklistService.isRefreshTokenBlacklisted(token)) {
            return false;
        }

        final String tokenType = extractTokenType(token);
        return "REFRESH".equals(tokenType) && isTokenValid(token);
    }

    private boolean isTokenValid(String token) {
        return !extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractTokenType(String token) {
            return extractClaim(token, claims -> claims.get("tokenType", String.class));
    }

    public UUID extractUserId(String token) {
        String userId = extractClaim(token, claims -> claims.get("userId", String.class));
        return UUID.fromString(userId);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey)getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getTokenRemainingTimeInMillis(String token) {
        Date expiration = extractExpiration(token);
        return Math.max(0, expiration.getTime() - System.currentTimeMillis());
    }
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    }