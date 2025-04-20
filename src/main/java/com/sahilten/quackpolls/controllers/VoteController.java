package com.sahilten.quackpolls.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahilten.quackpolls.domain.dto.vote.VoteRequestDto;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import com.sahilten.quackpolls.domain.mappers.VoteMapper;
import com.sahilten.quackpolls.security.user.QuackpollUserDetails;
import com.sahilten.quackpolls.services.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/polls/{pollId}/vote")
@RequiredArgsConstructor
@Slf4j
public class VoteController {

    private final VoteService voteService;
    private final VoteMapper voteMapper;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> vote(@AuthenticationPrincipal QuackpollUserDetails userDetails,
                                  @PathVariable("pollId") UUID pollId,
                                  @RequestBody VoteRequestDto voteRequest) throws JsonProcessingException {
        UserEntity userEntity = userDetails.getUserEntity();

        voteService.vote(pollId, voteRequest.getOptionId(), voteRequest.getVoterIdentifier(), userEntity);

        return ResponseEntity.ok().build();
    }
}
