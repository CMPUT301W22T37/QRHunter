package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button createAccountBtn;
    private Button btn;
    private String deviceID;
    private String userName = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.btnQRScanner);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ScanLoginCodeActivity.class));
            }
        });

        createAccountBtn = findViewById(R.id.btnCreateAccount);

        //Getting Device ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //Querying if that ID already exists
        queryIDs();
        //Username is not accurately set here
    }


    public void onCreateAccount(View view){
        startActivity(new Intent(MainActivity.this,CreateAccount.class));
    }

    public void queryIDs(){
        //Searching ID's for deviceID
        db = FirebaseFirestore.getInstance();
        db.collection("ID's")
                .whereEqualTo("ID", deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Device ID is found
                                findUserAndSignIn(document.getString("User Name"));
                            }
                        }
                    }
                });
    }

    public void findUserAndSignIn(final String userName){
        //Search for user in Users and start new activity with that user
        db.collection("Users")
                .whereEqualTo("User Name", userName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Context context = getApplicationContext();//Context for intent
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Get all Elements of the user
                                HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                                User user = new User(data);
                                //Start Activity with User
                                Intent intent = new Intent(context, MainMenu.class);
                                intent.putExtra("User",user);
                                startActivity(intent);
                            }
                        }
                    }
                });
    }



}