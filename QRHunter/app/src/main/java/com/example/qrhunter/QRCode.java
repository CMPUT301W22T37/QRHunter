package com.example.qrhunter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class to represent QR Codes as objects, using the hash as the unique identifier for QRCodes
 */

public class QRCode implements Serializable{
    private Integer score;
    private Integer ID;
    private String hash;
    private String image;
    private Integer lat;
    private Integer lon;

    /**
     * Constructor for QRCode object
     * @param code
     *      Code scanned from QR Code, used to calculate score
     * @param ID
     *      Unique Identifier for a QRCode
     */
    public QRCode(String code, Integer ID){
        this.ID = ID;
        this.hash = getHash(code);
        this.score = calculateScore(this.hash);
        this.lat = 0;
        this.lon = 0;
        this.image = "";
    }

    /**
     * Alternate constructor used to pass the score directly in from hashmap reconstruction
     * @param score
     *      score of the QRCode
     * @param ID
     *      Unique ID of the QRCode
     */
    public QRCode(Integer score, Integer ID, String hash, Integer lat, Integer lon, String image){
        this.hash = hash;
        this.score = score;
        this.ID = ID;
        this.lat = lat;
        this.lon = lon;
        this.image = image;
    }

    /**
     * Gets the latitude of the QRCode
     * @return
     *      Integer representing the latitude of the QRCode
     */
    public Integer getLat(){
        return lat;
    }

    /**
     * Get the longitude of the QRCode
     * @return
     *      Integer representing the longitude of the QRCode
     */
    public Integer getLon(){
        return lon;
    }

    /**
     * Get the image of the QRCode
     * @return
     *      String representing the QRCode's image
     */
    public String getImage(){
        return image;
    }

    /**
     * Gets the score of a particular QRCode
     * @return
     *      Score of the particular QRCode
     */
    public int getScore(){
        return score;
    }

    /**
     * Gets the SHA-256 Hash of the given code
     * @param code
     *      Code scanned from QR Code
     * @return
     *      String representing the SHA-256 hash of the code
     */
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

    /**
     * Calculates score of a code scanned in from a QRCode
     * @param sha256hex
     *      Code obtained from a QR Code
     * @return
     *      int representing the score of the QRCode
     */
    public int calculateScore(String sha256hex){
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

    /**
     * Mathematical function to calculate exponents, used for score
     * @param base
     *      Base of the exponent
     * @param exponent
     *      Exponent being applied on base
     * @return
     *      Result of the exponentiation of the base to the given exponent
     */
    static int power(int base, int exponent) {
        int power = 1;
        //increment the value of i after each iteration until the condition becomes false
        for (int i = 1; i <= exponent; i++)
            //calculates power
            power = power * base;
        //returns power
        return power;
    }

    /**
     * Getter for the ID of a QRCode
     * @return
     *      String representation of the chronological ID badge of a QRCode
     */
    public String getID(){
        return this.ID.toString();
    }

    /**
     * Gets the hash of the QRCode to be used as the unique identifier
     * @return
     */
    public String getUniqueHash(){
        return this.hash;
    }
}
