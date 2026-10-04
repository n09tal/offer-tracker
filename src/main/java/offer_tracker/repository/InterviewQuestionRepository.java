package offer_tracker.repository;

import offer_tracker.entity.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findByJobApplicationIdAndUserIdOrderByIdAsc(Long applicationId, Long userId);

    void deleteByJobApplicationIdAndUserId(Long applicationId, Long userId);
}