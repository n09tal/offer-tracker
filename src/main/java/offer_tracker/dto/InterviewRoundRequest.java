package offer_tracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import offer_tracker.entity.RoundResult;
import offer_tracker.entity.RoundType;

import java.time.LocalDateTime;

public record InterviewRoundRequest(
        @NotNull(message = "must not be null")
        RoundType roundType,

        @NotNull(message = "must not be null")
        LocalDateTime scheduledAt,

        RoundResult result,

        @Size(max = 2000, message = "must be at most 2000 characters")
        String notes
) {
}