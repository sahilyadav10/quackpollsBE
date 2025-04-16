package com.sahilten.quackpolls.domain.dto.poll;

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
public class PollDto {
    private UUID id;
    private UUID userId;
    private String question;
    private boolean isPublic;
    private LocalDateTime closesAt;
    private LocalDateTime createdAt;
}
