package offer_tracker.dto;

import offer_tracker.entity.ApplicationStatus;
import offer_tracker.entity.JobApplication;

import java.time.LocalDateTime;

public record JobApplicationResponse(
        Long id,
        String company,
        String position,
        ApplicationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static JobApplicationResponse from(JobApplication app) {
        return new JobApplicationResponse(
                app.getId(),
                app.getCompany(),
                app.getPosition(),
                app.getStatus(),
                app.getCreatedAt(),
                app.getUpdatedAt()
        );
    }
}