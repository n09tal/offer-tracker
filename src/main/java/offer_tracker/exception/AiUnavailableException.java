package offer_tracker.exception;

public class AiUnavailableException extends RuntimeException {
    public AiUnavailableException() {
        super("AI service is not configured");
    }
}