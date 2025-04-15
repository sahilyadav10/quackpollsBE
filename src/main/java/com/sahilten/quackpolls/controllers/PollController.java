package com.sahilten.quackpolls.controllers;

import com.sahilten.quackpolls.domain.dto.CreatePollRequest;
import com.sahilten.quackpolls.domain.dto.PollDto;
import com.sahilten.quackpolls.domain.entities.PollEntity;
import com.sahilten.quackpolls.domain.mappers.PollMapper;
import com.sahilten.quackpolls.repositories.PollRepository;
import com.sahilten.quackpolls.services.PollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/v1/polls")
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;
    private final PollMapper pollMapper;

    @PostMapping
    public ResponseEntity<PollDto> createPoll(@Valid @RequestBody CreatePollRequest createPollRequest) {
        log.info("Received poll: {}", createPollRequest);
        // Map DTO to Entity
        PollEntity pollEntity = pollMapper.toEntity(createPollRequest);

        // Save poll
        PollEntity savedPoll = pollService.save(pollEntity);

        // Manually map Entity to DTO for response
        PollDto response = pollMapper.toDto(savedPoll);
        return ResponseEntity.ok(response);
    }

}


