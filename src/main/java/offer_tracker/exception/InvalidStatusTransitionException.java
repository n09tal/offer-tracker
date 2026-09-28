package offer_tracker.exception;

import offer_tracker.entity.ApplicationStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(ApplicationStatus from, ApplicationStatus to) {
        super("Cannot change status from " + from + " to " + to);
    }
}