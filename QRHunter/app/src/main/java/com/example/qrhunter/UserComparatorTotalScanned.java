package com.example.qrhunter;

import java.util.Comparator;

/**
 * Class to sort users based on the number of QR Codes they have scanned
 */
public class UserComparatorTotalScanned implements Comparator<User> {
    /**
     * Gives ability to compare two Users based on their total number of QRCodes
     * @param user1
     *      First User being compared
     * @param user2
     *      Second User being compared
     * @return
     *      Returns int, 1 if user 1 is greater, -1, if user 2 is greater, 0 if equal
     */
    @Override
    public int compare(User user1, User user2) {
        int user1Num = user1.getNumCodes();
        int user2Num = user2.getNumCodes();

        if (user1Num < user2Num){
            return 1;
        }
        else if (user1Num >user2Num){
            return -1;
        }
        return 0;
    }
}
