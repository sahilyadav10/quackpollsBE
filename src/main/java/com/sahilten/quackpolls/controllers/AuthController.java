package com.sahilten.quackpolls.controllers;

import com.sahilten.quackpolls.domain.dto.auth.AuthResponse;
import com.sahilten.quackpolls.domain.dto.auth.LoginRequest;
import com.sahilten.quackpolls.domain.dto.auth.RegisterRequest;
import com.sahilten.quackpolls.domain.mappers.UserMapper;
import com.sahilten.quackpolls.services.AuthenticationService;
import com.sahilten.quackpolls.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private ResponseCookie buildCookie(String name, String value, long maxAge, String path) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path(path)
                .maxAge(maxAge)
                .sameSite("None")
                .build();
    }

    private ResponseEntity<Void> tokensToResponse(AuthResponse tokens, HttpStatus status) {
        ResponseCookie accessToken = buildCookie("access_token", tokens.getAccessToken(), accessTokenExpiration, "/");
        ResponseCookie refreshToken = buildCookie("refresh_token", tokens.getRefreshToken(), refreshTokenExpiration,
                "refresh");

        return ResponseEntity.status(status)
                .header(HttpHeaders.SET_COOKIE, accessToken.toString())
                .header(HttpHeaders.SET_COOKIE, refreshToken.toString())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authenticationService.register(request);

        AuthResponse tokens =
                authenticationService.authenticate(request.getEmail(),
                        request.getPassword());

        return tokensToResponse(tokens, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse tokens =
                authenticationService.authenticate(request.getEmail(),
                        request.getPassword());

        return tokensToResponse(tokens, HttpStatus.OK);
    }

    /**
     * Issue a fresh access token (and optionally rotate the refresh token) using the HTTP‑only
     * refresh_token cookie. The browser must include the cookie automatically.
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthResponse authResponse = authenticationService.refreshToken(refreshToken);
        String accessToken = authResponse.getAccessToken();
        Date expiry = jwtService.extractExpiryFromToken(accessToken);

        ResponseCookie accessTokenCookie =
                buildCookie("access_token", authResponse.getAccessToken(), accessTokenExpiration, "/");
        ResponseCookie refreshTokenCookie =
                buildCookie("refresh_token", authResponse.getRefreshToken(), refreshTokenExpiration, "/refresh");

        Map<String, Object> body = new HashMap<>();
        body.put("accessTokenExpiresAt", expiry.getTime());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(body);
    }
}
