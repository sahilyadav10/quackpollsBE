package com.sahilten.quackpolls.services;

import com.sahilten.quackpolls.domain.entities.PollEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PollService {
    PollEntity save(PollEntity pollEntity);

    Optional<PollEntity> get(UUID pollId);

    PollEntity update(PollEntity pollEntity);

    void delete(UUID pollId);

    List<PollEntity> getAllForUser(UUID userId);
}
