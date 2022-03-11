package com.example.qrhunter;

import java.util.Comparator;

/**
 * allows values in the QR code to be compared
 */
public class QRCodeComparator implements Comparator<QRCode> {
    /**
     * the compare function to compare scores of QR codes
     * @param code1
     *      the first code to be compared
     * @param code2
     *      the second code to be compared
     * @return
     *      returns which code is greater
     */
    @Override
    public int compare(QRCode code1, QRCode code2) {
        if (code1.getScore() > code2.getScore()){
            return 1;
        }
        else if (code1.getScore() < code2.getScore()){
            return -1;
        }
        else{
            return 0;
        }
    }
}
