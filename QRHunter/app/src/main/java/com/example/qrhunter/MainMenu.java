package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The main menu activity. This is page the user sees first when they are signed in
 */
public class MainMenu extends AppCompatActivity {
    private ArrayAdapter<String> codesAdapter;
    private ListView codesListView;
    private User user;
    private DataManagement dataManager;
    private ArrayList<String> codesDisplay;
    private TextView totalScore;
    private TextView totalScanned;


    /**
     * called when creating the activity
     * @param savedInstanceState
     *      the instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("QRHunter");



        new PermissionChecker(MainMenu.this);
        //Getting user
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);

        codesListView = findViewById(R.id.QRCode_List_View);
        totalScanned = findViewById(R.id.num_scanned_text);
        totalScore = findViewById(R.id.total_score_text);
        totalScanned.setText("Codes Scanned: " + Integer.toString(user.getAllCodes().size()));
        totalScore.setText("Total Score: "+ Integer.toString(user.getTotalScore()));

        //Creating Listview for QRCodes
        codesDisplay = user.getCodesStrings();
        codesAdapter = new ArrayAdapter<String>(this, R.layout.qr_list, codesDisplay);
        codesListView.setAdapter(codesAdapter);

        codesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int i, long l) {
                String selected =(String) (codesListView.getItemAtPosition(i));
                QRCode qrCode = user.getCode(user.getCodesStrings().indexOf(selected));
                Context context = getApplicationContext();
                Intent intent =new Intent(context, QrCodePage.class);
                intent.putExtra("QRCode",qrCode);
                intent.putExtra("User",user);
                startActivity(intent);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * sets the user of the main menu
     * @param user
     *      the user to be changed to
     */
    public void setUser(User user){
        if(user==null){
            Log.d("TAG", "User DNE");
            return;
        }
        this.user = user;

        this.codesDisplay = this.user.getCodesStrings();
        this.codesListView.setAdapter(this.codesAdapter);
        codesAdapter.notifyDataSetChanged();
        Log.d("TAG", "New user length"+Integer.toString(user.getCodesStrings().size()));
    }

    /**
     * Called when code is scanned
     * @param view
     *      View for the button clicked
     */
    public void onScan(View view){
        Intent intent = new Intent(this, ScanQRCodeActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    /**
     * Called when stats button is clicked
     * @param view
     *      View for the button clicked
     */
    public void onStatsCLick(View view){
        onGeneralClick(true);
    }

    public void onPlayersClick(View view){
        onGeneralClick(false);
    }

    /**
     * Generalized method that takes the user to either the stats page or player page
     * @param stats
     *      boolean variable of whether to go to stats or profile
     */
    public void onGeneralClick(final boolean stats){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<User> allUsersTemp = new ArrayList<>();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User((HashMap<String, Object>) document.getData());
                                allUsersTemp.add(user);
                            }
                            if (stats){
                                Intent intent = new Intent(getApplicationContext(), StatsPage.class);
                                intent.putExtra("User", user);
                                intent.putExtra("AllUsers", allUsersTemp);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), PlayersPage.class);
                                intent.putExtra("AllUsers", allUsersTemp);
                                startActivity(intent);
                            }
                        }
                    }
                });
    }

    /**
     * Called when the Profile button is clicked
     * @param view
     *      View for the button clicked
     */
    public void onProfileClick(View view){
        Intent intent = new Intent(this, ProfilePage.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    public void onMapClick(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    public void onSearch(View view){
        Intent intent = new Intent(this, SearchQRPage.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    /**
     * https://developer.android.com/training/appbar/actions
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_icon:
                Intent intent = new Intent(this, ProfilePage.class);
                intent.putExtra("User", user);
                startActivity(intent);
                return true;

            case R.id.stats_icon:
                onGeneralClick(true);
                return true;

            case R.id.search_person:
                onGeneralClick(false);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}