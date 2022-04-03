package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QRSocialPage extends AppCompatActivity {
    private QRCode qrCode;
    private User user;
    private TextView title;
    private ListView peopleList;
    private List<String> allUsers;
    private ArrayAdapter<String> usersAdapter;
    private User searchedUser;

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

        //To go to player search page from clicking listview
        peopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int i, long l) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final ArrayList<User> allUsersTemp = new ArrayList<>();
                String userName =(String) (peopleList.getItemAtPosition(i));

                db.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        User user = new User((HashMap<String, Object>) document.getData());
                                        if (user.getUsername().equals(userName)){
                                            searchedUser = user;
                                        }
                                        allUsersTemp.add(user);

                                        Intent intent = new Intent(getApplicationContext(), PlayersPage.class);
                                        intent.putExtra("AllUsers", allUsersTemp);
                                        intent.putExtra("User", user);
                                        intent.putExtra("SearchedUser", searchedUser);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
            }
        });
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