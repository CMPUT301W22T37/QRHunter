package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class OwnerActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private SwipeMenuListView codeListView;
    private SwipeMenuListView userListView;
    private ArrayList<String> codeDisplayList = new ArrayList<String>();
    private ArrayList<String> userDisplayList = new ArrayList<String>();
    private ArrayAdapter<String> codeListAdapter;
    private ArrayAdapter<String> userListAdapter;
    private String state = "Codes";
    private String selection;
    CollectionReference qrCodesReference;
    CollectionReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        codeListView = (SwipeMenuListView) findViewById(R.id.code_list_view);
        userListView = (SwipeMenuListView) findViewById(R.id.user_list_view);
        db = FirebaseFirestore.getInstance();
        qrCodesReference = db.collection("QRCodes");
        usersReference = db.collection("Users");

        codeListAdapter = new ArrayAdapter<String>(this, R.layout.qr_list_condensed, codeDisplayList);
        userListAdapter = new ArrayAdapter<String>(this, R.layout.qr_list_condensed, userDisplayList);
        codeListView.setAdapter(codeListAdapter);
        userListView.setAdapter(userListAdapter);

        getUsersList();
        getQRList();

//        codeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
//            {
//                selection = adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), selection, Toast.LENGTH_LONG).show();
//            }
//
//        });
//
//        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
//            {
//                selection = adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), selection, Toast.LENGTH_LONG).show();
//            }
//
//        });
        SwipeMenuCreator creatorUser = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(180);
                // set a icon
                deleteItem.setIcon(R.drawable.black_trash_can);
                // add to menu
                menu.addMenuItem(deleteItem);

                SwipeMenuItem addOwnerItem = new SwipeMenuItem(
                        getApplicationContext());

                addOwnerItem.setBackground(new ColorDrawable(Color.rgb(255,
                        153, 0)));

                addOwnerItem.setWidth(180);

                addOwnerItem.setIcon(R.drawable.black_star);

                menu.addMenuItem(addOwnerItem);
            }
        };
        SwipeMenuCreator creatorQR = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(180);
                // set a icon
                deleteItem.setIcon(R.drawable.black_trash_can);
                menu.addMenuItem(deleteItem);

            }
        };

        //Set creator
        codeListView.setMenuCreator(creatorQR);

        codeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        selection = codeDisplayList.get(position);
                        deleteSelected(selection);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        userListView.setMenuCreator(creatorUser);

        userListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        selection = userDisplayList.get(position);
                        deleteSelected(selection);
                        break;

                    case 1:
                        selection = userDisplayList.get(position);
                        setOwner(selection);

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
//
    }

    public void setOwner(String username){
        db.collection("Users")
                .whereEqualTo("User Name",username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Get all Elements of the user
                                HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                                User user = new User(data);
                                user.setOwner(true);

                                DataManagement dataManager = new DataManagement(user,db);
                                dataManager.updateData();
                                getUsersList();


//                                                                           dataManager.removeCode(QRCode());
                            }
                        }
                    }
                });
    }
//
    public void showUsers(View view){
        state = "Users";
        selection = null;
        codeListView.setVisibility(View.GONE);
        userListView.setVisibility(View.VISIBLE);
    }
    public void getUsersList(){
        userDisplayList.clear();
        usersReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                                boolean owner = (boolean)data.get("owner");
                                String identifier;
                                if(owner){
                                    identifier = "*"+document.getId();
                                }else{
                                    identifier = document.getId();
                                }
                                userDisplayList.add(identifier);
                            }
                            userListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    public void getQRList(){
        codeDisplayList.clear();
        qrCodesReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                codeDisplayList.add(document.getId());
                            }
                            codeListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void showCodes(View view){
        state = "Codes";
        selection = null;
        userListView.setVisibility(View.GONE);
        codeListView.setVisibility(View.VISIBLE);
    }

    public void deleteSelected(String selection){
        if (state == "Users"){
            db.collection("Users").document(selection).delete();
            getUsersList();
        } else if (state == "Codes"){
            db.collection("QRCodes").document(selection)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(Task<DocumentSnapshot> task) {
                            List<String> users = (List<String>)task.getResult().get("users");
                            db.collection("QRCodes").document(selection).delete();
                            getQRList();
                            for(String username: users){
                                Log.d("DEBUG","User: "+username);
                                db.collection("Users")
                                        .whereEqualTo("User Name", username)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        //Get all Elements of the user
                                                        HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                                                        User user = new User(data);
                                                        QRCode code = user.getCodeFromHash(selection);
                                                        if(code!=null){
                                                            Log.d("DEBUG","deleting code with hash"+code.getUniqueHash());
                                                            DataManagement dataManager = new DataManagement(user,db);
                                                            dataManager.removeCode(code, new CallBack() {
                                                                @Override
                                                                public void onCall(User user) {
                                                                    return;
                                                                }
                                                            });
                                                        }
//                                                                           dataManager.removeCode(QRCode());
                                                    }
                                                }
                                            }
                                        });
                            }

                        }


                    });
//


        }


    }


}