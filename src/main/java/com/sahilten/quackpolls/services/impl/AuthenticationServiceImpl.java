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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    // Dependencies injected by Spring via constructor (thanks to @RequiredArgsConstructor)
    private final AuthenticationManager authenticationManager; // Spring's AuthenticationManager
    //    private final UserDetailsService userDetailsService; // Service that loads user details (used for login)
    private final QuackpollUserDetailsService quackpollUserDetailsService;
    private final UserRepository userRepository; // Repository for accessing user data
    private final PasswordEncoder passwordEncoder; // Encoder to securely hash passwords
    private final JwtService jwtService; // Service responsible for generating and validating JWT tokens
    @Value("${jwt.expiration}")  // Inject expiration time from application.properties
    private long jwtExpiration;

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
                .build();

        userRepository.save(user);
    }

    @Override
    public AuthResponse authenticate(String email, String password) {
        // Authenticate the user by creating an AuthenticationToken with email and password
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // Once authentication is successful, load the user details (to generate the token)
        UserDetails userDetails = quackpollUserDetailsService.loadUserByUsername(email);

        // Generate the JWT token for the authenticated user
        String token = jwtService.generateToken(userDetails);

        // Return the AuthResponse with the generated token and its expiration time (e.g., 24 hours)
        return new AuthResponse(token, jwtExpiration);
    }

    /**
     * Validates the JWT token and extracts user details based on the token.
     *
     * @param token the JWT token sent by the client in the Authorization header
     * @return the authenticated UserDetails object corresponding to the token's user
     */
    @Override
    public UserDetails validateAndExtract(String token) {
        // Extract the username (email) from the JWT token
        String username = jwtService.extractUsername(token);

        // Get user details by username
        UserDetails userDetails = quackpollUserDetailsService.loadUserByUsername(username);

        // Validate the token
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("The token is invalid or expired.");
        }

        return userDetails;
    }
}
