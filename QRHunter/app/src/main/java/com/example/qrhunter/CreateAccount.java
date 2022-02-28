package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;

public class CreateAccount extends AppCompatActivity {
    private EditText userName;
    private EditText email;
    private FirebaseFirestore db;
    private String givenUserName;
    private String givenEmail;
    private String ERROR_MESSAGE = "Must Enter Email and Username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        userName = findViewById(R.id.Username_EditText);
        email = findViewById(R.id.email_EditText);
    }

    public void createAccount(View view){
        givenUserName = userName.getText().toString();
        givenEmail = email.getText().toString();

        //Checking that usernames are actually entered
        if (!(givenUserName.equals("") || givenEmail.equals(""))){
            addToDatabase(givenUserName);
//
        }
        else {
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this,ERROR_MESSAGE,duration);
            toast.show();
        }
    }

    public void addToDatabase(String givenUserName){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .whereEqualTo("User Name", givenUserName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Context context = getApplicationContext();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Toast that User name is not valid
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context,"User Name is Not Valid",duration);
                                toast.show();
                                return;//Return if not valid
                            }
                            createUser();//Call createUser if valid

                        }
                    }
                });
    }

    public void createUser(){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this,"User Name is Valid",duration);
        toast.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("Users");

        User user = new User(givenUserName, givenEmail);
        HashMap<String, Object> data = new HashMap<>();
        data.put("User Name", user.getUsername());
        data.put("Email", user.getEmail());
        data.put("QRCodes", user.getAllCodes());
        colRef
                .document(user.getUsername())
                .set(data);
        Intent intent =new Intent(this, MainMenu.class);
        intent.putExtra("User",user);
        startActivity(intent);
    }
}