package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class OwnerActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView codeListView;
    private ListView userListView;
    private ArrayList<String> codeDisplayList = new ArrayList<String>();
    private ArrayList<String> userDisplayList = new ArrayList<String>();
    private ArrayAdapter<String> codeListAdapter;
    private ArrayAdapter<String> userListAdapter;
    private String state = "Codes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        codeListView = (ListView)findViewById(R.id.code_list_view);
        userListView = (ListView)findViewById(R.id.user_list_view);
        db = FirebaseFirestore.getInstance();
        final CollectionReference qrCodesReference = db.collection("QRCodes");
        final CollectionReference usersReference = db.collection("Users");

        codeListAdapter = new ArrayAdapter<String>(this, R.layout.qr_list_condensed, codeDisplayList);
        userListAdapter = new ArrayAdapter<String>(this, R.layout.qr_list_condensed, userDisplayList);
        codeListView.setAdapter(codeListAdapter);
        userListView.setAdapter(userListAdapter);

        qrCodesReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                codeDisplayList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    codeDisplayList.add(doc.getId());
                }
                codeListAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });

        usersReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                userDisplayList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    userDisplayList.add(doc.getId());
                }
                userListAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });

    }

    public void showUsers(View view)
    {
        state = "Users";
        codeListView.setVisibility(View.GONE);
        userListView.setVisibility(View.VISIBLE);
    }

    public void showCodes(View view)
    {
        state = "Codes";
        userListView.setVisibility(View.GONE);
        codeListView.setVisibility(View.VISIBLE);
    }

}