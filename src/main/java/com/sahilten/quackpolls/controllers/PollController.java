package com.sahilten.quackpolls.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahilten.quackpolls.domain.dto.poll.CreatePollRequest;
import com.sahilten.quackpolls.domain.dto.poll.PollDto;
import com.sahilten.quackpolls.domain.dto.poll.PollWithVotesDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    public ResponseEntity<?> getPoll(@AuthenticationPrincipal QuackpollUserDetails userDetails, @PathVariable(
            "id") UUID pollId) {
        UserEntity userEntity = userDetails.getUserEntity();
        UUID currentUserId = userEntity.getId();

        PollEntity pollEntity = pollService.get(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        boolean isCreator = pollEntity.getUser().getId().equals(currentUserId);
        boolean isClosed = Optional.ofNullable(pollEntity.getClosesAt())
                .map(closesAt -> closesAt.isBefore(LocalDateTime.now()))
                .orElse(false);

        if (isCreator || isClosed) {
            PollWithVotesDto response = pollMapper.toWithVotesDto(pollEntity);
            return ResponseEntity.ok(response);
        } else {
            PollDto response = pollMapper.toDto(pollEntity);
            return ResponseEntity.ok(response);
        }

    }

    @GetMapping
    public ResponseEntity<List<PollDto>> getAllPolls(@AuthenticationPrincipal QuackpollUserDetails userDetails) {
        UserEntity userEntity = userDetails.getUserEntity();

        List<PollEntity> pollEntities = pollService.getAllForUser(userEntity.getId());

        List<PollDto> allPolls = new ArrayList<>();
        for (PollEntity entity : pollEntities) {
            allPolls.add(pollMapper.toDto(entity));
        }

        return ResponseEntity.ok(allPolls);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePoll(@PathVariable("id") UUID pollId) {
        Optional<PollEntity> pollEntity = pollService.get(pollId);

        if (pollEntity.isPresent()) {
            pollService.delete(pollId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


}
