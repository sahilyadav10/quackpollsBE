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
public class PollDto {
    private UUID id;
    private String question;
    private String lastName;
    private String email;
}
