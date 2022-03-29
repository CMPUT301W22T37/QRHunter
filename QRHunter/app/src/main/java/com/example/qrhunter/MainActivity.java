package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Activity that displays the sign in options screen of the app
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button btn;
    private String deviceID;

    /**
     * Called when created
     * @param savedInstanceState
     *      The instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Getting Device ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //Querying if that ID already exists
        queryIDs();

    }

    /**
     * Searches the ID's collection for the ID of the device that has just booted up the app
     */
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
//                            addToDatabase();
                        }
                    }
                });
    }

//    public void addToDatabase(){
//
//
//        //Adding User to the Database
//        while(true){
//            Random rand = new Random();
//            int upperbound = 1000000;
//            int random_user = rand.nextInt(upperbound);
//            String tempUser = "user"+random_user;
//            String tempEmail = "";
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            final User user = new User(tempUser,tempEmail);
//            final DataManagement manager = new DataManagement(user,db);
//            //Adding the ID into ID's
//            HashMap<String, String> ID = new HashMap<>();
//            ID.put("User Name", tempUser);
//            ID.put("ID", deviceID);
//
//            db.collection("ID's").document(deviceID)
//                    .set(ID);//No onSuccess or onFailure Listeners
//
//            db.collection("Users")
//                    .whereEqualTo("User Name", tempUser)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            Context context = getApplicationContext();
//                            if (task.isSuccessful()) {
//
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    //Toast that User name is not valid
//                                    Log.d("DEBUG",user.getUsername()+" not valid");
//                                    return;//Return if not valid
//                                }
//
//                                manager.updateData();
//                                Intent intent =new Intent(context, MainMenu.class);
//                                intent.putExtra("User",user);
//                                startActivity(intent);
//
//
//
//                            }
//                        }
//                    });
//        }
//
//    }

    /**
     * Called if device ID is already contained within ID's collection
     * Finds user name associated with ID and auto-signs them in
     * @param userName
     *      User name associated with the found ID
     */
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