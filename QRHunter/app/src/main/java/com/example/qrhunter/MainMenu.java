package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    private ArrayAdapter<String> codesAdapter;
    private ListView codesListView;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Getting user
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");

        codesListView = findViewById(R.id.QRCode_List_View);

        //Creating Listview for rolls
        codesAdapter = new ArrayAdapter<>(this, R.layout.activity_main_menu, user.getCodesStrings());
        codesListView.setAdapter(codesAdapter);
    }
}