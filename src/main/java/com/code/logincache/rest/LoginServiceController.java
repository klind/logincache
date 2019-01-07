package com.code.logincache.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.code.logincache.LoginService;

/**
 * LoginServiceController.
 */
@RestController
public class LoginServiceController {

    @Autowired
    private LoginService loginService;

    /**
     * Checks whether the user has logged in within the last 24 hours
     * does NOT imply a login.
     *
     * @param userid The user id
     * @return true if the user HAS logged in within 24 hours, otherwise false.
     */
    @RequestMapping(value = "/check24", method = RequestMethod.GET)
    public final boolean check24(@RequestParam(value = "userid") final String userid) {
        return loginService.hasUserLoggedInWithin24(userid);
    }

    /**
     * Sets the last login time for the user to now.
     * @param userid The user id
     */
    @RequestMapping(value = "/loggedin", method = RequestMethod.PUT)
    public final void loggedin(@RequestParam(value = "userid") final String userid) {
        loginService.userJustLoggedIn(userid);
    }

    /**
     * Validates if the specified user id is present in the cache.
     * @param userid The user id
     * @return boolean true if the specified user is in the cache.
     */
    @RequestMapping(value = "/userincache", method = RequestMethod.GET)
    public final boolean userincache(@RequestParam(value = "userid") final String userid) {
        return loginService.isUserInCache(userid);
    }

    /**
     * Return the current size of the cache.
     * @return int current size of the cache.
     */
    @RequestMapping(value = "/cachesize", method = RequestMethod.GET)
    public final int cachesize() {
        return loginService.getCacheSize();
    }
}
