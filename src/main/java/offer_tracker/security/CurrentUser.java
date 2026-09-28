package offer_tracker.security;

import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    // TODO: read the user id from the JWT token
    public Long id() {
        return 1L;
    }
}