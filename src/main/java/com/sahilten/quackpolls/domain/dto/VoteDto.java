package com.sahilten.quackpolls.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDto {
    private UUID id;
    private UUID pollId;
    private UUID optionId;
    private UUID userId; // optional – null for anonymous votes
    private LocalDateTime votedAt;
}
