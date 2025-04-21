package com.sahilten.quackpolls.domain.dto.poll;

import com.sahilten.quackpolls.domain.dto.option.OptionsWithVotesDto;
import com.sahilten.quackpolls.domain.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PollWithVotesDto {
    private UUID id;
    private UUID userId;
    private String question;
    private boolean isPublic;
    private LocalDateTime closesAt;
    private LocalDateTime createdAt;
    private UserDto creator;
    private List<OptionsWithVotesDto> options;
}
