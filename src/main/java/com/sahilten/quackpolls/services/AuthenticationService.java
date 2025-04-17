package com.sahilten.quackpolls.services;

import com.sahilten.quackpolls.domain.dto.auth.AuthResponse;
import com.sahilten.quackpolls.domain.dto.auth.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    void register(RegisterRequest registerRequest);

    AuthResponse authenticate(String email, String password);

    UserDetails validateAndExtract(String token);
}
