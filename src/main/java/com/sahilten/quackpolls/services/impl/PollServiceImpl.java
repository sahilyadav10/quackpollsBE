package com.sahilten.quackpolls.services.impl;

import com.sahilten.quackpolls.domain.entities.OptionEntity;
import com.sahilten.quackpolls.domain.entities.PollEntity;
import com.sahilten.quackpolls.repositories.PollRepository;
import com.sahilten.quackpolls.services.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private final PollRepository pollRepository;

    @Override
    public PollEntity save(PollEntity pollEntity) {
        for (OptionEntity optionEntity : pollEntity.getOptions()) {
            optionEntity.setPoll(pollEntity);
        }
        return pollRepository.save(pollEntity);
    }

    @Override
    public Optional<PollEntity> get(UUID pollId) {
        return pollRepository.findById(pollId);
    }

    @Override
    public PollEntity update(PollEntity pollEntity) {
        for (OptionEntity optionEntity : pollEntity.getOptions()) {
            optionEntity.setPoll(pollEntity);
        }
        return pollRepository.save(pollEntity);
    }

    @Override
    public void delete(UUID pollId) {
        pollRepository.deleteById(pollId);
    }

    @Override
    public List<PollEntity> getAllForUser(UUID userId) { // test‑friendly
        return pollRepository.findAllByUser_Id(userId);
    }

}
