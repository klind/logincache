package com.code.logincache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class LoginCacheApplicationTest {

    @Autowired
    private LoginService loginService;

    @Test
    void loginTest() {

        // New entry will not be added to the cache as 1234 is not in the database.
        assertFalse(loginService.hasUserLoggedInWithin24("1234"));

        // New entry will be added to the cache.
        loginService.hasUserLoggedInWithin24("1235");
        assertEquals(1, loginService.getCacheSize());
        // Check that 1235 was added to the cache.
        assertTrue(loginService.isUserInCache("1235"));

        // New entry will be added to the cache.
        loginService.hasUserLoggedInWithin24("1236");
        assertEquals(2, loginService.getCacheSize());

        // Add more entries to fill up the cache to its max
        loginService.hasUserLoggedInWithin24("1237");
        loginService.hasUserLoggedInWithin24("1238");
        loginService.hasUserLoggedInWithin24("1239");
        loginService.hasUserLoggedInWithin24("1240");
        loginService.hasUserLoggedInWithin24("1241");
        loginService.hasUserLoggedInWithin24("1242");
        loginService.hasUserLoggedInWithin24("1243");
        loginService.hasUserLoggedInWithin24("1244");
        assertEquals(10, loginService.getCacheSize());

        // Add one more entry to go beyond the max cache size.
        // Entry 1235 should now be removed from the cache and 1245 added
        loginService.hasUserLoggedInWithin24("1245");
        assertFalse(loginService.isUserInCache("1235"));
        assertEquals(10, loginService.getCacheSize());

        // Add one more entry to go beyond the max cache size.
        // Entry 1236 should now be removed from the cache and 1246 added
        loginService.hasUserLoggedInWithin24("1246");
        assertFalse(loginService.isUserInCache("1236"));
        assertEquals(10, loginService.getCacheSize());

        // Check userJustLoggedIn will add new entry
        loginService.userJustLoggedIn("1247");
        assertTrue(loginService.isUserInCache("1247"));
        assertEquals(10, loginService.getCacheSize());

        // Check that userJustLoggedIn is not adding again
        loginService.userJustLoggedIn("1247");
        assertEquals(10, loginService.getCacheSize());

    }

}
