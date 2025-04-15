package com.sahilten.quackpolls.services.impl;

import com.sahilten.quackpolls.domain.entities.PollEntity;
import com.sahilten.quackpolls.repositories.PollRepository;
import com.sahilten.quackpolls.services.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private final PollRepository pollRepository;

    @Override
    public PollEntity save(PollEntity pollEntity) {
        return pollRepository.save(pollEntity);
    }
}
