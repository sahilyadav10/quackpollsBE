package com.sahilten.quackpolls.controllers;

import com.sahilten.quackpolls.domain.dto.auth.AuthResponse;
import com.sahilten.quackpolls.domain.dto.auth.LoginRequest;
import com.sahilten.quackpolls.domain.dto.auth.RegisterRequest;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import com.sahilten.quackpolls.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private ResponseCookie buildCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("None")
                .build();
    }

    private ResponseEntity<UserEntity> tokensToResponse(AuthResponse tokens, HttpStatus status, UserEntity user) {
        ResponseCookie access = buildCookie("access_token", tokens.getAccessToken(), accessTokenExpiration);
        ResponseCookie refresh = buildCookie("refresh_token", tokens.getRefreshToken(), refreshTokenExpiration);
        return ResponseEntity.status(status)
                .header(HttpHeaders.SET_COOKIE, access.toString())
                .header(HttpHeaders.SET_COOKIE, refresh.toString())
                .body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@Valid @RequestBody RegisterRequest request) {
        authenticationService.register(request);

        List<Object> response =
                authenticationService.authenticate(request.getEmail(),
                        request.getPassword());
        AuthResponse tokens = (AuthResponse) response.getFirst();
        UserEntity userEntity = (UserEntity) response.get(1);
        return tokensToResponse(tokens, HttpStatus.CREATED, userEntity);
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@Valid @RequestBody LoginRequest request) {
        List<Object> response =
                authenticationService.authenticate(request.getEmail(),
                        request.getPassword());
        AuthResponse tokens = (AuthResponse) response.getFirst();
        UserEntity userEntity = (UserEntity) response.get(1);

        return tokensToResponse(tokens, HttpStatus.OK, userEntity);
    }

    /**
     * Issue a fresh access token (and optionally rotate the refresh token) using the HTTP‑only
     * refresh_token cookie. The browser must include the cookie automatically.
     */
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshAccessToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Authenticate and obtain new tokens
        AuthResponse authResponse = authenticationService.refreshToken(refreshToken);

        return tokensToResponse(authResponse, HttpStatus.OK);
    }
}
