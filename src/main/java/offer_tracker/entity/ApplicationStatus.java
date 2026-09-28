package offer_tracker.entity;

public enum ApplicationStatus {
    APPLIED,
    INTERVIEWING,
    OFFER,
    REJECTED;

    public boolean canTransitionTo(ApplicationStatus target) {
        return switch (this) {
            case APPLIED -> target == INTERVIEWING || target == REJECTED;
            case INTERVIEWING -> target == OFFER || target == REJECTED;
            case OFFER, REJECTED -> false;
        };
    }
}