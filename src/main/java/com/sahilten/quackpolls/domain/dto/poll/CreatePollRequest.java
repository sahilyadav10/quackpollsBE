package com.sahilten.quackpolls.domain.dto.poll;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sahilten.quackpolls.domain.dto.option.OptionDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePollRequest {

    @NotBlank(message = "Question is required")
    @Size(min = 2, max = 100, message = "Question must be between {min} and {max} characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s-?]+$",
            message = "Question can only contain letters, numbers, spaces, dashes and question marks"
    )
    private String question;

    private boolean isPublic = true;

    @Future(message = "Close time must be in the future")
    @NotNull(message = "Close time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closesAt;


    @Valid // run validation on each OptionDto
    @NotEmpty(message = "Add at least two options")
    @Size(min = 2, max = 10,
          message = "Provide {min}–{max} options")
    private List<OptionDto> options;
}
