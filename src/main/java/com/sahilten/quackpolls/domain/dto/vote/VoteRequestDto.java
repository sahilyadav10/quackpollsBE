package com.sahilten.quackpolls.domain.dto.vote;

import lombok.Getter;

import java.util.UUID;

@Getter
public class VoteRequestDto {
    private UUID optionId;
    private String voterIdentifier;
    
}
