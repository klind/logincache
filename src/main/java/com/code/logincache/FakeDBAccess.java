package com.code.logincache;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Fake DB class created to code challenge.
 */
@Component
public class FakeDBAccess {
    /**
     * Returns the last login time for the specified user id. If none found null is returned.
     * @param userId The user id.
     * @return LocalDateTime The last time the user logged in.
     */
    public final LocalDateTime getLastLoginForUser(final String userId) {
        LocalDateTime lastlogin = null;
        // I want to be able to test, that when checking a user that has never logged in, the result will be false.
        // If user id is 1234, the user has never logged in.
        if (!"1234".equals(userId)) {
            final double random = Math.random();
            if (random < .5) {
                lastlogin = LocalDateTime.now();
            } else {
                lastlogin = LocalDateTime.now().minusHours(42);
            }
        }
        return lastlogin == null ? null : lastlogin;
    }

    /**
     * Sets or updates the last login time for the specified user id.
     * @param userId The user id.
     * @param date The last time the user logged in.
     */
    public final void setLastLoginForUser(final String userId, final LocalDateTime date) {
        // do nothing
    }
}
