package com.example.qrhunter;

import java.util.ArrayList;

public class User {
    private String name;
    private String username;
    private String email;
    private ArrayList<String> allCodes;

    public User(String username, String email){
        this.username = username;
        this.email = email;
        this.allCodes = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getAllCodes() {
        return allCodes;
    }
}
