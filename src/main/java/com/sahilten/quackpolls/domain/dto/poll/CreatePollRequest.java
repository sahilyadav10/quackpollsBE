package com.sahilten.quackpolls.domain.dto.poll;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePollRequest {

    @NotBlank(message = "Question is required")
    @Size(min = 2, max = 100, message = "Question must be between {min} and {max} characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s-]+$",
            message = "Question can only contain letters, numbers, spaces, and common punctuation"
    )
    private String question;

    private boolean isPublic = true;

    @Future(message = "Close time must be in the future")
    private LocalDateTime closesAt;
}
