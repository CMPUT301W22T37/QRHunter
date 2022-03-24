package com.example.qrhunter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class searchPlayer extends AppCompatActivity {

    SearchView searchView;
    ListView listView;

//    String[] nameList = {"Klondike", "Lynnae", "Johnson", "Sooraj", "Logan", "Farse", "Potten","Marcus", "Sarah"};

    private List<String> playernames=new ArrayList<>();
//    String[] playernamesfinal=playernames.toArray(new String[0]);


    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_player);

        searchView = (SearchView) findViewById(R.id.search_bar);
        listView = (ListView) findViewById(R.id.list_item);


        final FirebaseFirestore db=FirebaseFirestore.getInstance();

        db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                playernames.clear();

                for(DocumentSnapshot snapshot : value) {
                    playernames.add(snapshot.getString("User Name"));
                }
                ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,playernames);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,playernames);
        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(searchPlayer.this, "You Click -"+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT);
//            }
//        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPlayer.this.arrayAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchPlayer.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        

    }


}