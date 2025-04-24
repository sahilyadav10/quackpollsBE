package com.sahilten.quackpolls.services.impl;

import com.sahilten.quackpolls.domain.dto.auth.AuthResponse;
import com.sahilten.quackpolls.domain.dto.auth.RegisterRequest;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import com.sahilten.quackpolls.exceptions.UserAlreadyExistsException;
import com.sahilten.quackpolls.repositories.UserRepository;
import com.sahilten.quackpolls.security.user.QuackpollUserDetailsService;
import com.sahilten.quackpolls.services.AuthenticationService;
import com.sahilten.quackpolls.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    // Dependencies injected by Spring via constructor (thanks to @RequiredArgsConstructor)
    private final AuthenticationManager authenticationManager; // Spring's AuthenticationManager
    private final QuackpollUserDetailsService quackpollUserDetailsService;
    private final UserRepository userRepository; // Repository for accessing user data
    private final PasswordEncoder passwordEncoder; // Encoder to securely hash passwords
    private final JwtService jwtService; // Service responsible for generating and validating JWT tokens

    @Override
    public void register(RegisterRequest registerRequest) {
        userRepository.findByEmail(registerRequest.getEmail()).ifPresent(user -> {
            // If a user with the same email exists, throw a custom exception
            throw new UserAlreadyExistsException("Email already registered.");
        });

        UserEntity user = UserEntity.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword())) // Encode the password before storing
                .gender(registerRequest.getGender())
                .age(registerRequest.getAge())
                .build();

        userRepository.save(user);
    }

    @Override
    public List<Object> authenticate(String email, String password) {
        // Authenticate the user by creating an AuthenticationToken with email and password
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // Once authentication is successful, load the user details (to generate the token)
        UserDetails userDetails = quackpollUserDetailsService.loadUserByUsername(email);

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(
                        "User not found with email: " + email));

        String accessToken = jwtService.generateToken(userDetails, false);
        String refreshToken = jwtService.generateToken(userDetails, true);


        // Return the AuthResponse with the generated tokens and its expiration time (e.g., 24 hours)
        return List.of(new AuthResponse(accessToken, refreshToken), userEntity);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token is missing.");
        }

        String email = jwtService.extractUsername(refreshToken);

        UserDetails userDetails = quackpollUserDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new RuntimeException("Refresh token is invalid or expired.");
        }

        // Generate new access and refresh tokens
        String accessToken = jwtService.generateToken(userDetails, false);
        String newRefreshToken = jwtService.generateToken(userDetails, true);

        return new AuthResponse(accessToken, newRefreshToken);
    }


    @Override
    public UserDetails validateAndExtract(String token) {
        // Extract the username (email) from the JWT token
        String username = jwtService.extractUsername(token);

        // Get user details by username
        UserDetails userDetails = quackpollUserDetailsService.loadUserByUsername(username);

        // Validate the token
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("The access token is invalid or expired.");
        }

        return userDetails;
    }
}
