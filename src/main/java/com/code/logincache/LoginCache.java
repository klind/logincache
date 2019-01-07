package com.code.logincache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Cache implementation.
 */
@Component
public class LoginCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginCache.class);
    // The max cache size
    private static final int MAX_CACHE = 10;
    // I use this FIFO list to keep track on the last user ids added to the cache.
    // This is used to easy remove the first added user id from the cache when the cache needs to be reduced.
    private static Queue<String> lastLoggedInUserIds = new LinkedList<>();
    // Use HashMap even though it is not thread safe as ConcurrentHashMap or HashTable.
    // In this case I synchronize on LoginCache itself, as we are also using a queue.
    // If no queue was used, I would have used ConcurrentHashMap
    private static Map<String, LocalDateTime> cache = new HashMap<>(MAX_CACHE);

    /**
     * Adds the specified user id with the specified time to the cache.
     * If the cache has reached its max size, the oldest entry will be removed.
     *
     * @param userId The user id.
     * @param now current time.
     */
    public final void addToCache(final String userId, final LocalDateTime now) {
        if (StringUtils.isEmpty(userId) || now == null) {
            LOGGER.warn("Cannot add empty/null user id or null login time to the cache");
            return;
        }
        if (cache.size() == MAX_CACHE && !cache.containsKey(userId)) {
            final String oldestUserId = lastLoggedInUserIds.remove();
            LOGGER.debug("Removed {} from lastLoggedInUserIds", oldestUserId);
            if (oldestUserId == null) {
                // If the lastLoggedInUserIds list is empty clear the cache.
                LOGGER.warn("Clearing the cache and lastLoggedInUserIds as {} was not found in lastLoggedInUserIds", oldestUserId);
                cache.clear();
                lastLoggedInUserIds.clear();
            } else {
                final LocalDateTime removedFromCache = cache.remove(oldestUserId);
                if (removedFromCache == null) {
                    // If the user id retrieved from lastLoggedInUserIds was not found on the cache clear the cache.
                    LOGGER.warn("Clearing the cache and lastLoggedInUserIds as {} was not found in cache, but was found in lastLoggedInUserIds ", oldestUserId);
                    cache.clear();
                    lastLoggedInUserIds.clear();
                } else {
                    LOGGER.debug("Removed {} from cache", oldestUserId);
                }
            }
        }
        lastLoggedInUserIds.add(userId);
        cache.put(userId, now);
        LOGGER.debug("Added {} to the cache", userId);
    }

    /**
     * Returns the current size of the cache.
     * @return int The size of the cache.
     */
    public final int getCacheSize() {
        return cache.size();
    }

    /**
     *
     * @param userId The user id.
     * @return boolean If cache contains the user id return true, otherwise false.
     */
    public final boolean isUserInCache(final String userId) {
        return cache.containsKey(userId);
    }

    /**
     * Returns the last login for the specified user id.
     * @param userId The user id.
     * @return LocalDateTime The last login time.
     */
    public final LocalDateTime get(final String userId) {
        return cache.get(userId);
    }
}
