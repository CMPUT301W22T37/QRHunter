package com.example.qrhunter;

import java.util.Comparator;

/**
 * Class to sort users based on the sum of the QRCodes they have scanned
 */
public class UserComparatorTotalSum implements Comparator<User> {
    /**
     * Gives ability to compare two Users based on their total sum of QRCodes
     * @param user1
     *      First User being compared
     * @param user2
     *      Second User being compared
     * @return
     *      Returns int, 1 if user 1 is less than, -1, if user 2 is less than, 0 if equal
     */
    @Override
    public int compare(User user1, User user2) {
        int user1Sum = user1.getTotalScore();
        int user2Sum = user2.getTotalScore();

        if (user1Sum < user2Sum){
            return 1;
        }
        else if (user1Sum >user2Sum){
            return -1;
        }
        return 0;
    }
}
