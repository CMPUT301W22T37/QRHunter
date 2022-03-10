package com.example.qrhunter;

import java.util.ArrayList;

/**
 * a helper interface used for interacting with the database
 */
public interface CallBack {
    /**
     * an action to be performed upon calling (including updating user)
     * @param user
     *      the user currently signed in
     */
    void onCall(User user);
}
