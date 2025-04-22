package com.library.library.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

//        @Value("${application.security.jwt.secret-key}")
//        private String secretKey;
//        @Value("${application.security.jwt.expiration}")
//        private long jwtExpiration;
//        @Value("${application.security.jwt.refresh-token.expiration}")
//        private long refreshExpiration;
        private final String secretKey = "f3a53bd31b02b6a27269dc1d0e9835ad6769d0a2e18f0df5e87182f29900140b";
        private final long jwtExpiration = 86400000L; // e.g., 1 day in milliseconds
        private final long refreshExpiration = 604800000L;

        public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //allows extracting claims of different data types (String, Date, Boolean, etc.)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); //Retrieves all claims from JWT
        return claimsResolver.apply(claims);
        /*Uses a function to extract a specific claim from the complete set.
        like String username = extractClaim(token, Claims::getSubject);
        Date expirationDate = extractClaim(token, Claims::getExpiration);
         */
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long jwtExpiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)  // Updated from setClaims
                .subject(userDetails.getUsername())  // Updated from setSubject
                .issuedAt(new Date(System.currentTimeMillis()))  // Updated from setIssuedAt "iat" claim
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))  // Updated from setExpiration
                .signWith(getSignInKey())  // Updated from signWith with algorithm parameter. algorithm inferred from key type
                .compact();
    }

    // check username equal to username in token and token not expired if ok RETURN TRUE
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //check token is not expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //return time of expiration
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
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
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //decodes into bytes
        return Keys.hmacShaKeyFor(keyBytes); // converts bytes into SecretKey compatible with the HMAC SHA algorithm
    }
    }