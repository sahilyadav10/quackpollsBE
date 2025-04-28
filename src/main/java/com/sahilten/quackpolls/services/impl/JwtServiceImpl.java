package com.sahilten.quackpolls.services.impl;

import com.sahilten.quackpolls.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;
    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(UserDetails userDetails, boolean isRefreshToken) {
        long expiration = isRefreshToken ? refreshTokenExpiration : accessTokenExpiration;

        // Create a JWT token with the following data (claims):
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // "sub" claim: the username or email of the user
                .setIssuedAt(new Date())                // "iat" claim: the time when the token was created
                .setExpiration(new Date(
                        System.currentTimeMillis() + expiration)) // "exp" claim: expiration time (e.g., 24 hours)
                .signWith(getSigningKey(),
                        SignatureAlgorithm.HS256)  // Sign the token using the secret key and HS256 algorithm
                .compact();  // Create and return the token as a string
    }


    @Override
    public String extractUsername(String token) {

        return extractClaims(token).getSubject();  // Extract and return the "sub" claim (here the email)
    }

    @Override
    public Date extractExpiryFromToken(String token) {
        return extractClaims(token).getExpiration();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // Extract the username from the token and check if it matches the provided user details
        final String username = extractUsername(token);
        // If the username in the token matches and the token isn't expired, it's valid
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        // Get the expiration date from the "exp" claim and compare it with the current time
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // Use the signing key to ensure the token is valid
                .build()
                .parseClaimsJws(token)  // Parse the JWT
                .getBody();  // Return the claims (data) from the token
    }

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
