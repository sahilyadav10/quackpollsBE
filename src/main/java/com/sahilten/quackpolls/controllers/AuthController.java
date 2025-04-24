package com.sahilten.quackpolls.controllers;

import com.sahilten.quackpolls.domain.dto.auth.AuthResponse;
import com.sahilten.quackpolls.domain.dto.auth.LoginRequest;
import com.sahilten.quackpolls.domain.dto.auth.RegisterRequest;
import com.sahilten.quackpolls.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        authenticationService.register(request);
        AuthResponse authResponse = authenticationService.authenticate(
                request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Received login for: {}", loginRequest.getEmail());
        AuthResponse authResponse = authenticationService.authenticate(
                loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }
}
