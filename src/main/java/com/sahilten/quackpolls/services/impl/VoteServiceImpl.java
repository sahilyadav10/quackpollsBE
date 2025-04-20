package com.sahilten.quackpolls.services.impl;

import com.sahilten.quackpolls.domain.entities.OptionEntity;
import com.sahilten.quackpolls.domain.entities.PollEntity;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import com.sahilten.quackpolls.domain.entities.VoteEntity;
import com.sahilten.quackpolls.repositories.OptionRepository;
import com.sahilten.quackpolls.repositories.PollRepository;
import com.sahilten.quackpolls.repositories.VoteRepository;
import com.sahilten.quackpolls.services.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;


    @Override
    public void vote(UUID pollId, UUID optionId, String voterIdentifier, UserEntity userEntity) {
        PollEntity pollEntity =
                pollRepository.findById(pollId).orElseThrow(() -> new IllegalArgumentException(("Poll not found!")));
        OptionEntity optionEntity =
                optionRepository.findById(optionId)
                        .orElseThrow(() -> new IllegalArgumentException(("Option not found!")));

        VoteEntity voteEntity = new VoteEntity();
        voteEntity.setPollEntity(pollEntity);
        voteEntity.setOptionEntity(optionEntity);
        voteEntity.setVoterIdentifier(voterIdentifier);
        voteEntity.setVotedAt(LocalDateTime.now());
        voteEntity.setUserEntity(userEntity);
        voteRepository.save(voteEntity);
    }
}
