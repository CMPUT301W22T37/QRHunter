package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    private ArrayAdapter<String> codesAdapter;
    private ListView codesListView;
    private User user;
    private DataManagement dataManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Getting user
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        dataManager = new DataManagement(user);


        codesListView = findViewById(R.id.QRCode_List_View);

        //Creating Listview for rolls
        codesAdapter = new ArrayAdapter<String>(this, R.layout.qr_list, user.getCodesStrings());
        codesListView.setAdapter(codesAdapter);
    }
}