package com.sahilten.quackpolls.services;

import com.sahilten.quackpolls.domain.dto.auth.AuthResponse;
import com.sahilten.quackpolls.domain.dto.auth.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AuthenticationService {
    void register(RegisterRequest registerRequest);

    List<Object> authenticate(String email, String password);

    AuthResponse refreshToken(String refreshToken);

    UserDetails validateAndExtract(String token);
}
