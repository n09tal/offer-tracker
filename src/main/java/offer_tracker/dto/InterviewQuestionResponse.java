package offer_tracker.dto;

import offer_tracker.entity.InterviewQuestion;

import java.time.LocalDateTime;

public record InterviewQuestionResponse(
        Long id,
        String question,
        LocalDateTime createdAt
) {
    public static InterviewQuestionResponse from(InterviewQuestion q) {
        return new InterviewQuestionResponse(q.getId(), q.getQuestion(), q.getCreatedAt());
    }
}