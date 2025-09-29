//package com.eventconnect.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//    
//    @Value("${jwt.secret}")
//    private String secret;
//    
//    @Value("${jwt.expiration}")
//    private Long expiration;
//    
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes());
//    }
//    
//    public String generateToken(String email, Long userId) {
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("userId", userId)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//    
//    public String extractEmail(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//    
//    public Long extractUserId(String token) {
//        return extractClaim(token, claims -> claims.get("userId", Long.class));
//    }
//    
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//    
//    public <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.resolve(claims);
//    }
//    
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//    
//    public Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//    
//    public Boolean validateToken(String token, String email) {
//        final String tokenEmail = extractEmail(token);
//        return (tokenEmail.equals(email) && !isTokenExpired(token));
//    }
//    
//    @FunctionalInterface
//    public interface ClaimsResolver<T> {
//        T resolve(Claims claims);
//    }
//}


package com.eventconnect.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component // Marks this class as a Spring-managed component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret; // Secret key for signing JWTs

    @Value("${jwt.expiration}")
    private Long expiration; // Token expiration time in milliseconds

    // Generate the signing key from the secret
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate a JWT token with email and userId as claims
    public String generateToken(String email, Long userId) {
        return Jwts.builder()
                .setSubject(email)                       // Set the subject (email)
                .claim("userId", userId)                 // Add userId claim
                .setIssuedAt(new Date())                 // Set issued time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Set expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign token
                .compact();                              // Build the token
    }

    // Extract email (subject) from token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract userId from token
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract any claim using a resolver function
    public <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.resolve(claims);
    }

    // Parse all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Set signing key
                .build()
                .parseClaimsJws(token)          // Parse JWT
                .getBody();                     // Return claims
    }

    // Check if the token is expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate token by checking email match and expiration
    public Boolean validateToken(String token, String email) {
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }

    // Functional interface for extracting claims
    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}
