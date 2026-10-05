package offer_tracker;

import offer_tracker.entity.ApplicationStatus;
import offer_tracker.entity.JobApplication;
import offer_tracker.exception.InvalidStatusTransitionException;
import offer_tracker.exception.ResourceNotFoundException;
import offer_tracker.repository.JobApplicationRepository;
import offer_tracker.security.CurrentUser;
import offer_tracker.service.JobApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository repository;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private JobApplicationService service;

    @Test
    void illegalTransitionIsRejectedAndNotSaved() {
        when(currentUser.id()).thenReturn(1L);
        when(repository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(application(ApplicationStatus.APPLIED)));

        assertThrows(InvalidStatusTransitionException.class,
                () -> service.updateStatus(1L, ApplicationStatus.OFFER));

        verify(repository, never()).saveAndFlush(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void anotherUsersApplicationIsNotFound() {
        when(currentUser.id()).thenReturn(2L);
        when(repository.findByIdAndUserId(1L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    private JobApplication application(ApplicationStatus status) {
        JobApplication app = new JobApplication();
        app.setId(1L);
        app.setUserId(1L);
        app.setCompany("Google");
        app.setPosition("SDE");
        app.setStatus(status);
        return app;
    }
}