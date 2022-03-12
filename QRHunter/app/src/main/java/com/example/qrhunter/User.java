package com.example.qrhunter;

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

    /**
     * Main constructor for the user class
     * @param username
     *      the username for the user
     * @param email
     *      the email for the user
     */
    public User(String username, String email){//Take out ID
        this.username = username;
        this.email = email;
        this.allCodes = new ArrayList<>();

        //Testing Purposes only
        addCode(new QRCode("BFG5DGW54",getNextID()));
        addCode(new QRCode("DCFJFJFJ", getNextID()));
    }

    /**
     * Alternate constructor for User class, used for auto sign-in
     * @param data
     *      Hashmap retrieved from firebase containing a user
     */
    public User(HashMap<String, Object> data){
        this.email = (String)data.get("Email");
        this.username = (String)data.get("User Name");
        this.allCodes = hashToQRCode((ArrayList<HashMap>)data.get("QRCodes"));
    }

    /**
     * Converts a hashmap of QRCode contents into an Arraylist of QRCodes
     * @param maps
     *      Hashmap of QRCode contents to be converted
     * @return
     *      ArrayList of QRCodes
     */
    private ArrayList<QRCode> hashToQRCode(ArrayList<HashMap> maps){
        ArrayList<QRCode> codes = new ArrayList<>();
        for (HashMap code: maps) {
            String ID = (String)code.get("id");
            codes.add(new QRCode((String)code.get("code"), Integer.parseInt(ID)));
        }
        return codes;
    }

    /**
     * Getter for the user email
     * @return
     *      returns the user's email
     */
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
  
    /**
     * adds a QR code to the users profile
     * @param code
     *      code to be added
     * @return
     *      whether or not the operation was successful
     */
    public boolean addCode(QRCode code){
        boolean success = true;
        for(int i=0;i<this.allCodes.size();i++){
            if(allCodes.get(i).getID().equals(code.getID())){
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
            if(allCodes.get(i).getID().equals(code.getID())){
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
            codeStrings.add("QR Code #" + this.allCodes.get(i).getID());
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

    /**
     * returns the ID badge to be used for the next QRCode being added
     * @return
     *      ID badge for the next code
     */
    public int getNextID(){
        int size = allCodes.size();
        if (size == 0){
            return 1;//First QRCode gets a 1 ID
        }
        int max = 1;
        for (QRCode code:getAllCodes()) {
            if (Integer.parseInt(code.getID()) > max){
                max = Integer.parseInt(code.getID());
            }
        }
        return max + 1;//Else, return 1 more than the highest QRCode ID
    }                  //No two stored QRCodes can have the same ID

}
