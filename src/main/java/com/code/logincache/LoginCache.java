package com.code.logincache;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cache implementation.
 */
@Component
public class LoginCache extends LinkedHashMap<String, LocalDateTime> {
    // The max cache size
    private static final int MAX_CACHE = 10;

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_CACHE;
    }
}
