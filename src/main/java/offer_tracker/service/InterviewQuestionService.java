package offer_tracker.service;

import lombok.RequiredArgsConstructor;
import offer_tracker.ai.AiQuestionClient;
import offer_tracker.dto.GenerateQuestionsRequest;
import offer_tracker.dto.InterviewQuestionResponse;
import offer_tracker.entity.InterviewQuestion;
import offer_tracker.entity.JobApplication;
import offer_tracker.exception.ResourceNotFoundException;
import offer_tracker.repository.InterviewQuestionRepository;
import offer_tracker.repository.JobApplicationRepository;
import offer_tracker.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final InterviewQuestionRepository questionRepository;
    private final JobApplicationRepository applicationRepository;
    private final AiQuestionClient aiQuestionClient;
    private final TransactionTemplate transactionTemplate;
    private final CurrentUser currentUser;

    public List<InterviewQuestionResponse> generate(Long applicationId, GenerateQuestionsRequest request) {
        Long userId = currentUser.id();
        JobApplication app = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found: " + applicationId));

        List<String> questions = aiQuestionClient.generateQuestions(
                app.getCompany(), app.getPosition(), request.jobDescription());

        List<InterviewQuestion> saved = transactionTemplate.execute(status -> {
            questionRepository.deleteByJobApplicationIdAndUserId(applicationId, userId);
            List<InterviewQuestion> entities = questions.stream()
                    .map(text -> {
                        InterviewQuestion q = new InterviewQuestion();
                        q.setJobApplication(app);
                        q.setUserId(userId);
                        q.setQuestion(text);
                        return q;
                    })
                    .toList();
            return questionRepository.saveAll(entities);
        });

        return saved.stream().map(InterviewQuestionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<InterviewQuestionResponse> list(Long applicationId) {
        Long userId = currentUser.id();
        applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found: " + applicationId));
        return questionRepository.findByJobApplicationIdAndUserIdOrderByIdAsc(applicationId, userId).stream()
                .map(InterviewQuestionResponse::from)
                .toList();
    }
}