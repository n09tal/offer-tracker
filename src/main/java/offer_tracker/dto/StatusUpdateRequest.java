package offer_tracker.dto;

import jakarta.validation.constraints.NotNull;
import offer_tracker.entity.ApplicationStatus;

public record StatusUpdateRequest(
        @NotNull(message = "must not be null")
        ApplicationStatus status
) {
}