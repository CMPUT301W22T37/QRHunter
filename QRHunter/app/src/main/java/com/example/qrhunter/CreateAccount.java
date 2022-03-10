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


/**
 * Activity to create an account
 */
public class CreateAccount extends AppCompatActivity {
    private EditText userName;
    private EditText email;
    private FirebaseFirestore db;
    private String givenUserName;
    private String givenEmail;
    private String ERROR_MESSAGE = "Must Enter Email and Username";

    /**
     * called when created
     * @param savedInstanceState
     *      the instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        userName = findViewById(R.id.Username_EditText);
        email = findViewById(R.id.email_EditText);
    }

    /**
     * called when create account button is pressed
     * @param view
     *      the current view for the page
     */
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

    /**
     * interacts with the database manager to add the user to the database
     * upon success, sign into the account
     * @param givenUserName
     *      the desired username
     */
    public void addToDatabase(String givenUserName){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final User user = new User(givenUserName, givenEmail);
        final DataManagement manager = new DataManagement(user,db);
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
                                Toast toast = Toast.makeText(context,"Username is not valid, please select another",duration);
                                toast.show();
                                return;//Return if not valid
                            }

                            manager.updateData();
                            Intent intent =new Intent(context, MainMenu.class);
                            intent.putExtra("User",user);
                            startActivity(intent);

                        }
                    }
                });
    }


}