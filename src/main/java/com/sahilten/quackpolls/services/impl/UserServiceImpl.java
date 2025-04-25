package com.sahilten.quackpolls.services.impl;

import com.sahilten.quackpolls.domain.entities.UserEntity;
import com.sahilten.quackpolls.repositories.UserRepository;
import com.sahilten.quackpolls.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity get(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                "User not found with email: " + email));

        return userEntity;
    }
}
