package com.example.qrhunter;

import java.util.Comparator;

/**
 * Class to compare Users based on High Score
 */
public class UserComparatorHighScore implements Comparator<User> {
    /**
     * Compare function to compare Highscores
     * @param user1
     *      First user being passed in to compare
     * @param user2
     *      Second user being passed in to compare
     * @return
     *      int value, -1 if less than, 0 if equal, and 1 is greater than
     */
    @Override
    public int compare(User user1, User user2) {
        int user1Highest = user1.getHighest();
        int user2Highest = user2.getHighest();

        if (user1Highest < user2Highest){
            return 1;
        }
        else if (user1Highest > user2Highest){
            return -1;
        }
        return 0;
    }
}
