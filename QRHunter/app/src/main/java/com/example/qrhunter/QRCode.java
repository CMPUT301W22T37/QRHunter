package com.example.qrhunter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class QRCode implements Serializable{
    private String code;
    private int score;
    private int IDBadge; //Not implemented yet

    public QRCode(String code){
        this.code = code;
        this.score = calculateScore(code);
    }

    public int getScore(){
        return score;
    }

    public String getHash(String code){
        code = code + "\n";
        //For getting a SHA-256 hash of QRCode contents
        //Website:https://www.baeldung.com/sha-256-hashing-java
        //User:https://www.baeldung.com/author/baeldung/
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedHash = digest.digest(code.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (int i = 0; i < encodedHash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedHash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();//Returns this
    }

    public int calculateScore(String str){
        String sha256hex = getHash(str);

        int score=0;
        for (int i = 0; i < sha256hex.length(); i++) {
            // Counting occurrences of s[i]
            int count = 1;
            while (i + 1 < sha256hex.length() && sha256hex.charAt(i) == sha256hex.charAt(i + 1)) {
                i++;
                count++;
            }
            if(count>1) {
                int a=Character.getNumericValue(sha256hex.charAt(i));
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
