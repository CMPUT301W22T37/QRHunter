package com.example.qrhunter;

import java.util.List;

/**
 * the callback for comments
 */
public interface CommentCall {
    /**
     * an action to be completed when comments are called
     * @param comments
     *      the list of comments
     */
    void onCall(List<String> comments);
}
