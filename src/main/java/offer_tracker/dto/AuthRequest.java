package offer_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "must not be blank")
        @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "must not be blank")
        @Size(min = 8, max = 72, message = "must be between 8 and 72 characters")
        String password
) {
}