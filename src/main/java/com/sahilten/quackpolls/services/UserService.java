package com.sahilten.quackpolls.services;

import com.sahilten.quackpolls.domain.entities.UserEntity;

public interface UserService {
    public UserEntity get(String email);
}
