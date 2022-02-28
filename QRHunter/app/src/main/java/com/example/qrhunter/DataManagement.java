package com.example.qrhunter;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;

public class DataManagement  {
    private User user;
    private FirebaseFirestore db;
    private CollectionReference userRef;

    public DataManagement(User user){
        this.user = user;
        this.db = FirebaseFirestore.getInstance();
        this.userRef = db.collection("Users");
    }

    public void updateData(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("User Name", user.getUsername());
        data.put("Email", user.getEmail());
        data.put("QRCodes", user.getAllCodes());
        userRef
                .document(user.getUsername())
                .set(data);
    }

}
