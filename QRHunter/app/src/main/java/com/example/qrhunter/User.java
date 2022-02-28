package com.example.qrhunter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Serializable {
    private String name;
    private String username;
    private String email;
    private ArrayList<QRCode> allCodes;
    private DataManagement dataManager;

    public User(String username, String email){
        this.username = username;
        this.email = email;
        this.allCodes = new ArrayList<>();
        this.dataManager = new DataManagement(this);

        //Testing Purposes only
        addCode(new QRCode("ABCDEFG"));
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

    public void addCode(QRCode code){
        this.allCodes.add(code);
        Collections.sort(this.allCodes, new QRCodeComparator());
        dataManager.updateData();
    }

    public void removeQRCode(QRCode code){
        this.allCodes.remove(code);
        dataManager.updateData();
    }

    public ArrayList<String> getCodesStrings(){
        ArrayList<String> codeStrings = new ArrayList<>();
        for (int i = 0; i < this.allCodes.size(); i++) {
            codeStrings.add(this.allCodes.get(i).getCode());
        }
        return codeStrings;
    }

}
