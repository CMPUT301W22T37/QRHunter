package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User testUser = new User("testUser","test@gmail.com");
        HashMap<String, String> data = new HashMap<>();
        data.put(testUser.getUsername(), testUser.getEmail());
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        collectionReference
                .document(testUser.getUsername())
                .set(data);
    }


}