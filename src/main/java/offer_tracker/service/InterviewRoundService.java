package offer_tracker.service;

import lombok.RequiredArgsConstructor;
import offer_tracker.dto.InterviewRoundRequest;
import offer_tracker.dto.InterviewRoundResponse;
import offer_tracker.entity.ApplicationStatus;
import offer_tracker.entity.InterviewRound;
import offer_tracker.entity.JobApplication;
import offer_tracker.exception.ResourceNotFoundException;
import offer_tracker.repository.InterviewRoundRepository;
import offer_tracker.repository.JobApplicationRepository;
import offer_tracker.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewRoundService {

    private final InterviewRoundRepository roundRepository;
    private final JobApplicationRepository applicationRepository;
    private final CurrentUser currentUser;

    @Transactional
    public InterviewRoundResponse create(Long applicationId, InterviewRoundRequest request) {
        JobApplication app = findOwnedApplication(applicationId);
        if (app.getStatus() == ApplicationStatus.APPLIED) {
            app.setStatus(ApplicationStatus.INTERVIEWING);
        }

        InterviewRound round = new InterviewRound();
        round.setJobApplication(app);
        round.setUserId(currentUser.id());
        applyRequest(round, request);
        return InterviewRoundResponse.from(roundRepository.save(round));
    }

    @Transactional(readOnly = true)
    public List<InterviewRoundResponse> list(Long applicationId) {
        findOwnedApplication(applicationId);
        return roundRepository
                .findByJobApplicationIdAndUserIdOrderByScheduledAtAsc(applicationId, currentUser.id())
                .stream()
                .map(InterviewRoundResponse::from)
                .toList();
    }

    @Transactional
    public InterviewRoundResponse update(Long applicationId, Long roundId, InterviewRoundRequest request) {
        InterviewRound round = findOwnedRound(applicationId, roundId);
        applyRequest(round, request);
        return InterviewRoundResponse.from(roundRepository.saveAndFlush(round));
    }

    @Transactional
    public void delete(Long applicationId, Long roundId) {
        roundRepository.delete(findOwnedRound(applicationId, roundId));
    }

    private void applyRequest(InterviewRound round, InterviewRoundRequest request) {
        round.setRoundType(request.roundType());
        round.setScheduledAt(request.scheduledAt());
        round.setNotes(request.notes());
        if (request.result() != null) {
            round.setResult(request.result());
        }
    }

    private JobApplication findOwnedApplication(Long applicationId) {
        return applicationRepository.findByIdAndUserId(applicationId, currentUser.id())
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found: " + applicationId));
    }

    private InterviewRound findOwnedRound(Long applicationId, Long roundId) {
        return roundRepository.findByIdAndJobApplicationIdAndUserId(roundId, applicationId, currentUser.id())
                .orElseThrow(() -> new ResourceNotFoundException("Interview round not found: " + roundId));
    }
}