package com.example.qrhunter;

import java.io.Serializable;
import java.lang.reflect.Array;
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
    private boolean isOwner;

    /**
     * Main constructor for the user class
     * @param username
     *      the username for the user
     * @param email
     *      the email for the user

     */
    public User(String username, String email){
        this.username = username;
        this.email = email;
        this.allCodes = new ArrayList<>();
        this.isOwner = false;

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
        this.isOwner = (boolean)data.get("owner");
    }

    /**
     * Clears all codes that a user has stored
     */
    public void clearCodes(){
        this.allCodes.clear();
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
            codes.add(new QRCode(Integer.parseInt(ID),(String)code.get("uniqueHash"),
                    (double)code.get("latitude"), (double)code.get("longitude"),
                    (String)code.get("image")));
        }
        return codes;
    }

    /**
     * Updates a QRCode to include new comments
     * @param code
     *      QRCode to be updated
     */
    public void updateCodeComments(QRCode code){
        QRCode current;
        for (int i = 0; i < allCodes.size(); i++) {
            current = allCodes.get(i);
            if (current.getUniqueHash() == code.getUniqueHash()){
                allCodes.remove(current);
            }
        }
        allCodes.add(code);
    }

    /**
     * Returns an ArrayList of all SHA-256 Hashes currently stored
     * @return
     *      ArrayList of hashes
     */
    public ArrayList<String> getAllHashes(){
        ArrayList<String> hashes = new ArrayList<>();
        for (QRCode code:getAllCodes()) {
            hashes.add(code.getUniqueHash());
        }
        return hashes;
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

    /**
     * Returns highest score of a stored QRCode
     * @return
     *      Integer representing the score of the highest scoring QRCode
     */
    public Integer getHighest(){
        int size = getAllCodes().size();
        if(size==0){
            return 0;
        }
        QRCode highest = getAllCodes().get(size - 1);//Last QRCode is the highest
        return highest.getScore();
    }

    /**
     * Returns lowest score of a store QRCode
     * @return
     *      Integer representing the score of the lowest scoring QRCode
     */
    public Integer getLowest() {
        int size = getAllCodes().size();
        if(size==0){
            return 0;
        }
        QRCode lowest = getAllCodes().get(0);//First QRCode is the lowest
        return lowest.getScore();
    }

    /**
     * Updates the QRCode at a given index in the user's array of QRCodes
     * Note: Doesn't maintain sorted order, meant to be used with same code but different lat/lon/image
     * @param i
     *      The index at which to update
     * @param qrCode
     *      The new QRCode to update the existing one to be
     */
    public void updateCode(int i,QRCode qrCode){
        this.allCodes.set(i,qrCode);
    }

    public boolean getOwner(){
        return this.isOwner;
    }
    public void setOwner(boolean owner){
        this.isOwner = owner;
    }

    public QRCode getCodeFromHash(String hash){
        for(QRCode code: this.allCodes){
            if(code.getUniqueHash().equals(hash)){
                return code;
            }
        }
        return null;
    }
}
