package com.sahilten.quackpolls.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    String generateToken(UserDetails userDetails, boolean isRefreshToken);

    String extractUsername(String token);

    Date extractExpiryFromToken(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}
