package com.sahilten.quackpolls.domain.dto.option;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionsWithVotesDto {
    private int voteCount;
    private UUID id;
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s-?]+$",
            message = "Question can only contain letters, numbers, spaces, dashes and question marks"
    )
    @NotBlank(message = "Option text is required")
    private String text;
}
