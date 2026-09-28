package offer_tracker.repository;

import offer_tracker.entity.InterviewRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRoundRepository extends JpaRepository<InterviewRound, Long> {

    List<InterviewRound> findByJobApplicationIdAndUserIdOrderByScheduledAtAsc(Long applicationId, Long userId);

    Optional<InterviewRound> findByIdAndJobApplicationIdAndUserId(Long id, Long applicationId, Long userId);
}