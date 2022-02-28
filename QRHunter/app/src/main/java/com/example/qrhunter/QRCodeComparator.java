package com.example.qrhunter;

import java.util.Comparator;

public class QRCodeComparator implements Comparator<QRCode> {

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
