package com.sahilten.quackpolls.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    // A DTO is meant to carry just the data that your API needs to expose. PollEntity may contain sensitive data (e.g., private polls).
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
}
