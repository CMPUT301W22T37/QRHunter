package com.example.qrhunter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class User implements Serializable{
    private String username;
    private String email;
    private ArrayList<QRCode> allCodes;
    private ArrayList<String> IDs;
    private String test;

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

    public String getUsername() {
        return username;
    }

    public ArrayList<QRCode> getAllCodes() {
        return allCodes;
    }

    public ArrayList<String> getIDs(){
        return this.IDs;
    }

    public boolean addCode(QRCode code){
        boolean success = true;
        for(int i=0;i<this.allCodes.size();i++){
            if(allCodes.get(i).getCode().equals(code.getCode())){
                success = false;
                break;
            }
        }
        if(success){
            this.allCodes.add(code);
            Collections.sort(this.allCodes, new QRCodeComparator());
        }
        return success;
    }

    public boolean removeQRCode(QRCode code){
        boolean success = false;
        for(int i=0;i<this.allCodes.size();i++){
            if(allCodes.get(i).getCode().equals(code.getCode())){
                this.allCodes.remove(i);
                success = true;
                break;
            }
        }
        return success;
    }

    public ArrayList<String> getCodesStrings(){
        ArrayList<String> codeStrings = new ArrayList<>();
        for (int i = 0; i < this.allCodes.size(); i++) {
            codeStrings.add(this.allCodes.get(i).getCode());
        }
        return codeStrings;
    }
    public QRCode getCode(int index){
        return this.allCodes.get(index);
    }

    public int getTotalScore(){
        int score = 0;
        for(int i=0;i<this.allCodes.size();i++){
            score += this.allCodes.get(i).getScore();
        }
        return score;
    }

}
