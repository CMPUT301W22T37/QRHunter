package com.example.qrhunter;

import org.apache.commons.codec.digest.DigestUtils;

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

    public int calculateScore(String str){
        String sha256hex = DigestUtils.sha256Hex(str);
        int score=0;
        for (int i = 0; i < str.length(); i++) {
            // Counting occurrences of s[i]
            int count = 1;
            while (i + 1 < str.length() && str.charAt(i) == str.charAt(i + 1)) {
                i++;
                count++;
            }
            if(count>1) {
                int a=Character.getNumericValue(sha256hex.charAt(i));
//                System.out.println(Integer.parseInt(String.valueOf(sha256hex.charAt(i)),16)+ "=" + count + " ");
                score=score+power(a,count-1);
            }
        }
        return score;
    }

    public String getCode(){
        return code;
    }

    static int power(int base, int exponent) {
        int power = 1;
        //increment the value of i after each iteration until the condition becomes false
        for (int i = 1; i <= exponent; i++)
            //calculates power
            power = power * base;
        //returns power
        return power;
    }
}
