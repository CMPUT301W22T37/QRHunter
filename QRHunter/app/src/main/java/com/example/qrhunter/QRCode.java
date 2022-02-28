package com.example.qrhunter;

import java.io.Serializable;
import java.util.Random;

public class QRCode implements Serializable{
    private String code;
    private int score;

    public QRCode(String code){
        this.code = code;
        this.score = calculateScore(code);
    }

    public int getScore(){
        return score;
    }

    public int calculateScore(String string){
        //Sharu

        //Temp
        Random rand = new Random();
        return rand.nextInt(101);
    }

    public String getCode(){
        return code;
    }
}
