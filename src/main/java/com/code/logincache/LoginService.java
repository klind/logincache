package com.code.logincache;

/**
 * LoginService interface.
 */
public interface LoginService {
    /**
     * Checks whether the user has logged in within the last 24 hours
     * does NOT imply a login.
     *
     * @param userId The user id.
     * @return true if the user HAS logged in within 24 hours, otherwise false.
     */
    boolean hasUserLoggedInWithin24(String userId);

    /**
     * Sets the last login time for the user to now.
     *
     * @param userId The user id.
     */
    void userJustLoggedIn(String userId);

    /**
     * Return the current size of the cache.
     * @return int current size of the cache.
     */
    int getCacheSize();

    /**
     * Validates if the specified user id is present in the cache.
     * @param userId The user id.
     * @return boolean true if the specified user is in the cache.
     */
    boolean isUserInCache(String userId);
}
