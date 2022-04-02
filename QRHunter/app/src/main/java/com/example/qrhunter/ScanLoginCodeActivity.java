package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import java.util.HashMap;

/**
 * the activity to scan the login code
 */
public class ScanLoginCodeActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private FirebaseFirestore db;
    private User user;

    /**
     * called when activity created
     * @param savedInstanceState
     *      the instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_login_code);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("QRHunter");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");

        CodeScannerView scannerView = findViewById(R.id.login_scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanLoginCodeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

                        findUserAndSignIn(result.getText());

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

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
                            boolean found = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Get all Elements of the user
                                found = true;
                                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                //Adding the ID into ID's
                                HashMap<String, String> ID = new HashMap<>();
                                ID.put("User Name", userName);
                                ID.put("ID", deviceID);

                                db.collection("ID's").document(deviceID)
                                        .set(ID);//No onSuccess or onFailure Listeners
                                
                                HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                                User user = new User(data);
                                //Start Activity with User
                                Intent intent = new Intent(context, MainMenu.class);
                                intent.putExtra("User",user);
                                startActivity(intent);
                            }
                            if(!found){
                                Toast toast = Toast.makeText(context, "User not found", Toast.LENGTH_LONG);
                                toast.show();
                                finish();
                            }

                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                intent.putExtra("User",user);
                startActivity(intent);
                return true;
        }
        return true;
    }
}
