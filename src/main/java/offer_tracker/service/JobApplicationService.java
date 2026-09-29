package offer_tracker.service;

import offer_tracker.entity.ApplicationStatus;
import offer_tracker.exception.InvalidStatusTransitionException;
import offer_tracker.security.CurrentUser;

import lombok.RequiredArgsConstructor;
import offer_tracker.dto.JobApplicationRequest;
import offer_tracker.dto.JobApplicationResponse;
import offer_tracker.entity.JobApplication;
import offer_tracker.exception.ResourceNotFoundException;
import offer_tracker.repository.JobApplicationRepository;
import offer_tracker.dto.PageResponse;
import offer_tracker.repository.JobApplicationSpecifications;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository repository;
    private final CurrentUser currentUser;

    @Transactional
    public JobApplicationResponse create(JobApplicationRequest request) {
        JobApplication app = new JobApplication();
        app.setUserId(currentUser.id());
        app.setCompany(request.company());
        app.setPosition(request.position());
        return JobApplicationResponse.from(repository.save(app));
    }

    @Transactional(readOnly = true)
    public PageResponse<JobApplicationResponse> search(String company, ApplicationStatus status, int page, int size) {
        Specification<JobApplication> spec = Specification.allOf(
                JobApplicationSpecifications.belongsTo(currentUser.id()),
                JobApplicationSpecifications.hasStatus(status),
                JobApplicationSpecifications.companyContains(company)
        );
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return PageResponse.from(repository.findAll(spec, pageable).map(JobApplicationResponse::from));
    }

    @Transactional(readOnly = true)
    public JobApplicationResponse getById(Long id) {
        return JobApplicationResponse.from(findOwned(id));
    }

    @Transactional
    public JobApplicationResponse update(Long id, JobApplicationRequest request) {
        JobApplication app = findOwned(id);
        app.setCompany(request.company());
        app.setPosition(request.position());
        return JobApplicationResponse.from(repository.saveAndFlush(app));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findOwned(id));
    }

    @Transactional
    public JobApplicationResponse updateStatus(Long id, ApplicationStatus newStatus) {
        JobApplication app = findOwned(id);
        ApplicationStatus current = app.getStatus();
        if (!current.canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException(current, newStatus);
        }
        app.setStatus(newStatus);
        return JobApplicationResponse.from(repository.saveAndFlush(app));
    }

    private JobApplication findOwned(Long id) {
        return repository.findByIdAndUserId(id, currentUser.id())
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found: " + id));
    }
}