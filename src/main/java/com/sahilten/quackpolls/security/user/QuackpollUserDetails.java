package com.sahilten.quackpolls.security.user;

import com.sahilten.quackpolls.domain.entities.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Custom implementation of Spring Security's UserDetails interface,
 * wrapping around our application's UserEntity.
 * This is used by Spring Security to authenticate and authorize users.
 */
@Getter
@RequiredArgsConstructor // Automatically generates constructor for final field 'userEntity'
public class QuackpollUserDetails implements UserDetails {

    // The user entity we wrap and expose to Spring Security
    private final UserEntity userEntity;

    /**
     * Returns the authorities granted to the user.
     * For now, we assign a default "ROLE_USER".
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Returns the user's password.
     * Used internally by Spring Security for authentication.
     */
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    /**
     * Returns the user's username (in our case, email).
     * This is the unique identifier used to authenticate.
     */
    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    /**
     * Indicates whether the user's account has expired.
     * Always returns true in our case (no expiry logic).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     * Always returns true (no lockout logic implemented).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * Always returns true (no expiry logic).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     * Always returns true (all users are enabled by default).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Helper method to access the user's ID from outside the security context.
     */
    public UUID getUserId() {
        return userEntity.getId();
    }
}
