package com.sahilten.quackpolls.services;

import com.sahilten.quackpolls.domain.entities.PollEntity;

public interface PollService {
    PollEntity save(PollEntity pollEntity);
}