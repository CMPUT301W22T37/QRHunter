package com.example.qrhunter;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class represents a user
 */
public class User implements Serializable{
    private String username;
    private String email;
    private ArrayList<QRCode> allCodes;
    private ArrayList<String> IDs;
    private String test;

    /**
     * Constructor for the user class
     * @param username
     *      the username for the user
     * @param email
     *      the email for the user
     * @param ID
     *      the ID of the device signed in
     */
    public User(String username, String email, String ID){
        this.username = username;
        this.email = email;
        this.allCodes = new ArrayList<>();
        this.IDs = new ArrayList<>();
        addID(ID);

        //Testing Purposes only
        addCode(new QRCode("BFG5DGW54"));
        addCode(new QRCode("DCFJFJFJ"));
    }

    /**
     * Getter for the user email
     * @return
     *      returns the user's email
     */
    public User(HashMap<String, Object> data){
        this.email = (String)data.get("Email");
        this.username = (String)data.get("User Name");
        this.IDs = (ArrayList<String>)data.get("IDs");
        this.allCodes = hashToQRCode((ArrayList<HashMap>)data.get("QRCodes"));
    }

    private ArrayList<QRCode> hashToQRCode(ArrayList<HashMap> maps){
        ArrayList<QRCode> codes = new ArrayList<>();
        for (HashMap code: maps) {
            codes.add(new QRCode((String)code.get("code")));
        }
        return codes;
    }

    public void addID(String ID){
        this.IDs.add(ID);
    }

    public String getEmail() {
        return email;
    }

    /**
     * Getter for the username
     * @return
     *      the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the list of QR codes the user has scanned
     * @return
     *      an arraylist containing the codes
     */
    public ArrayList<QRCode> getAllCodes() {
        return allCodes;
    }

    public ArrayList<String> getIDs(){
        return this.IDs;
    }
  
    /**
     * adds a QR code to the users profile
     * @param
     *      code to be added
     * @return
     *      whether or not the operation was successful
     */
    public boolean addCode(QRCode code){
        boolean success = true;
        for(int i=0;i<this.allCodes.size();i++){
            if(allCodes.get(i).getCode().equals(code.getCode())){
                success = false; //already exists, do not add it again
                break;
            }
        }
        if(success){
            this.allCodes.add(code);
            Collections.sort(this.allCodes, new QRCodeComparator());
        }
        return success;
    }

    /**
     * removes a QR code from the user's profile
     * @param code
     *      the code to be removed
     * @return
     *      whether or not the code was successfully removed
     */
    public boolean removeQRCode(QRCode code){
        boolean success = false;
        for(int i=0;i<this.allCodes.size();i++){
            if(allCodes.get(i).getCode().equals(code.getCode())){
                this.allCodes.remove(i); //found the code so it can be removed
                success = true;
                break;
            }
        }
        return success;
    }

    /**
     * obtains a string representation of the list of codes
     * @return
     *      an array of strings with the QR code identifiers
     */
    public ArrayList<String> getCodesStrings(){
        ArrayList<String> codeStrings = new ArrayList<>();
        for (int i = 0; i < this.allCodes.size(); i++) {
            codeStrings.add(this.allCodes.get(i).getCode());
        }
        return codeStrings;
    }

    /**
     * get a code from the user given the index
     * @param index
     *      the position of code to grab
     * @return
     *      the QR code in that position
     */
    public QRCode getCode(int index){
        return this.allCodes.get(index); //only used internally in positions that are known to exist
    }

    /**
     * gets the total score of all codes
     * @return
     *      the total score
     */
    public int getTotalScore(){
        int score = 0;
        for(int i=0;i<this.allCodes.size();i++){
            score += this.allCodes.get(i).getScore();
        }
        return score;
    }

    /**
     * returns the total number of QR codes scanned
     * @return
     *      the number of codes scanned by the user
     */
    public int getNumCodes(){
        return this.allCodes.size();
    }

    public void updateCode(int i,QRCode qrCode){
        Log.d("DEBUG","Current i: "+ i + " current code: "+ this.allCodes.get(i).getCode() + " new code: " + qrCode.getCode());
        this.allCodes.set(i,qrCode);
    }

}
