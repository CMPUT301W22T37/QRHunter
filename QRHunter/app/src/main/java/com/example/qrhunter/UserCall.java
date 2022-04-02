package com.example.qrhunter;

import java.util.List;

/**
 * the helper interface for obtaining users from the database
 */
public interface UserCall {
    /**
     * helper function for users
     * @param users
     *      all users from the database
     */
    void onCall(List<String> users);
}
