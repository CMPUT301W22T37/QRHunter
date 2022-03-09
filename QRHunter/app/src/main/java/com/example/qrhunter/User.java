package com.example.qrhunter;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Serializable{
    private String name;
    private String username;
    private String email;
    private ArrayList<QRCode> allCodes;
//    private DataManagement dataManager;

    public User(String username, String email){
        this.username = username;
        this.email = email;
        this.allCodes = new ArrayList<>();

        //Testing Purposes only
        addCode(new QRCode("BFG5DGW54"));
        addCode(new QRCode("DCFJFJFJ"));
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

//        if(success){
//            Log.d("TAG", "Deleted");
//        }else{
//            Log.d("TAG", "Failed");
//        }
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
