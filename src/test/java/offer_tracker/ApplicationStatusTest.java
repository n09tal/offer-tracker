package offer_tracker;

import offer_tracker.entity.ApplicationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationStatusTest {

    @Test
    void appliedCanMoveToInterviewingOrRejected() {
        assertTrue(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.INTERVIEWING));
        assertTrue(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.REJECTED));
    }

    @Test
    void appliedCannotJumpToOffer() {
        assertFalse(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.OFFER));
    }

    @Test
    void offerAndRejectedAreFinal() {
        assertFalse(ApplicationStatus.OFFER.canTransitionTo(ApplicationStatus.REJECTED));
        assertFalse(ApplicationStatus.REJECTED.canTransitionTo(ApplicationStatus.APPLIED));
    }
}