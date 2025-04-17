package com.sahilten.quackpolls.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahilten.quackpolls.domain.dto.poll.CreatePollRequest;
import com.sahilten.quackpolls.domain.dto.poll.PollDto;
import com.sahilten.quackpolls.domain.entities.PollEntity;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import com.sahilten.quackpolls.domain.mappers.PollMapper;
import com.sahilten.quackpolls.security.user.QuackpollUserDetails;
import com.sahilten.quackpolls.services.PollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/v1/polls")
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;
    private final PollMapper pollMapper;
    private final ObjectMapper objectMapper;

    @GetMapping("/{id}")
    public ResponseEntity<PollDto> getPoll(@PathVariable("id") UUID pollId) {
        Optional<PollEntity> pollEntity = pollService.get(pollId);

        if (pollEntity.isPresent()) {
            PollDto response = pollMapper.toDto(pollEntity.get());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PollDto> createPoll(
            @AuthenticationPrincipal QuackpollUserDetails userDetails,
            @Valid @RequestBody CreatePollRequest createPollRequest) throws JsonProcessingException {

        UserEntity userEntity = userDetails.getUserEntity();

        // Map DTO to Entity
        PollEntity pollEntity = pollMapper.toEntity(createPollRequest);
        log.warn("Mapped PollEntity: {}",
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pollEntity));
        pollEntity.setUser(userEntity);

        // Save poll
        PollEntity savedPoll = pollService.save(pollEntity);

        // Manually map Entity to DTO for response
        PollDto response = pollMapper.toDto(savedPoll);
        return ResponseEntity.ok(response);
    }

}
