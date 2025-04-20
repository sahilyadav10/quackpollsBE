package com.sahilten.quackpolls.services;

import com.sahilten.quackpolls.domain.entities.UserEntity;

import java.util.UUID;

public interface VoteService {
    void vote(UUID pollId, UUID optionId, String voterIdentifier, UserEntity userEntity);
}
