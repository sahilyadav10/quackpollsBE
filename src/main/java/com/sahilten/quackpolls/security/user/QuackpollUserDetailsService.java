package com.sahilten.quackpolls.security.user;

import com.sahilten.quackpolls.domain.entities.UserEntity;
import com.sahilten.quackpolls.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * Responsible for loading user-specific data during authentication.
 */
@RequiredArgsConstructor
@Service
public class QuackpollUserDetailsService implements UserDetailsService {

    // Repository to access users from the database
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user from DB or throw if not found
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(
                        "User not found with email: " + email));

        // Wrap the user entity in a custom UserDetails implementation
        return new QuackpollUserDetails(userEntity);
    }
}
