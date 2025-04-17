package com.sahilten.quackpolls.config;

import com.sahilten.quackpolls.security.jwt.JwtAuthenticationFilter;
import com.sahilten.quackpolls.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService) {
        return new JwtAuthenticationFilter(authenticationService);
    }

    @Bean
    /**
     * Defines the security filter chain for handling HTTP requests.
     * - All incoming requests pass through this filter chain.
     * - Allows public access to specific endpoints like:
     *     - POST /v1/auth/**
     * - Requires authentication for all other requests.
     * - CSRF protection is disabled for stateless APIs.
     * - Configures the session policy as stateless (no server-side session).
     * - JWT Authentication Filter is inserted before the default UsernamePasswordAuthenticationFilter.
     *   - The JWT filter checks the Authorization header for a valid token.
     *   - If a valid token is found, the user is authenticated without using session-based login.
     *   - If an invalid token is found, it logs the error and proceeds with the request.
     *   - UsernamePasswordAuthenticationFilter does not interfere with JWT-based authentication as it runs after the JWT filter.
     */
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/v1/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()).
                sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Adding the JwtAuthenticationFilter before the default UsernamePasswordAuthenticationFilter.
                // This ensures JWT authentication is checked before form-based or basic login authentication.
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
