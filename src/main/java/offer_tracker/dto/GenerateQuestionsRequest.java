package offer_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenerateQuestionsRequest(
        @NotBlank(message = "must not be blank")
        @Size(max = 10000, message = "must be at most 10000 characters")
        String jobDescription
) {
}