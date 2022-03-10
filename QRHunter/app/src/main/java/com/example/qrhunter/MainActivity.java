package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button createAccountBtn;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        User testUser = new User("testUser1", "test1@gmail.com");
//        HashMap<String, String> data = new HashMap<>();
//        data.put(testUser.getUsername(), testUser.getEmail());
//        db = FirebaseFirestore.getInstance();
//        final CollectionReference collectionReference = db.collection("Users");
//        collectionReference
//                .document(testUser.getUsername())
//                .set(data);
        btn=(Button)findViewById(R.id.btnQRScanner);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ScanLoginCodeActivity.class));
            }
        });

        createAccountBtn = findViewById(R.id.btnCreateAccount);
    }

    public void onCreateAccount(View view){
        startActivity(new Intent(MainActivity.this,CreateAccount.class));
    }



}