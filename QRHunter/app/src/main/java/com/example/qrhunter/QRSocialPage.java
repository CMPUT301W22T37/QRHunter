package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class QRSocialPage extends AppCompatActivity {
    private QRCode qrCode;
    private User user;
    private TextView title;
    private ListView peopleList;
    private List<String> allUsers;
    private ArrayAdapter<String> usersAdapter;

    private DataManagement dataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrsocial_page);

        Intent intent = getIntent();
        qrCode = (QRCode) intent.getSerializableExtra("QRCode");
        user = (User) intent.getSerializableExtra("User");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);


        title = findViewById(R.id.number_people);
        peopleList = findViewById(R.id.People_List_View);
        getUsers();


    }
    public void getUsers(){
        Context context = getApplicationContext();
        dataManager.retrievePeople(qrCode ,new UserCall() {
            @Override
            public void onCall(List<String> users) {
                allUsers = users;
                if(allUsers==null){
                    title.setText("0 users have scanned this QR Code"); //error
                    return;
                }
                if(allUsers.size()==1){
                    title.setText(allUsers.size() + " user has scanned this QR Code");
                }else{
                    title.setText(allUsers.size() + " users have scanned this QR Code");

                }
                usersAdapter = new ArrayAdapter<String>(context, R.layout.distance_list, allUsers);
                peopleList.setAdapter(usersAdapter);
            }
        });
    }
}