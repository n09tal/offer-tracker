package offer_tracker.dto;

import offer_tracker.entity.InterviewRound;
import offer_tracker.entity.RoundResult;
import offer_tracker.entity.RoundType;

import java.time.LocalDateTime;

public record InterviewRoundResponse(
        Long id,
        Long applicationId,
        RoundType roundType,
        LocalDateTime scheduledAt,
        RoundResult result,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static InterviewRoundResponse from(InterviewRound round) {
        return new InterviewRoundResponse(
                round.getId(),
                round.getJobApplication().getId(),
                round.getRoundType(),
                round.getScheduledAt(),
                round.getResult(),
                round.getNotes(),
                round.getCreatedAt(),
                round.getUpdatedAt()
        );
    }
}