package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private SwipeMenuListView codesListView;
    private User user;
    private DataManagement dataManager;
    private ArrayList<String> codesDisplay;
    private TextView totalScore;
    private TextView totalScanned;
    private FloatingActionButton owner_button;
    private boolean userIsOwner;


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

        //Getting user and firebase reference
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);

        //Finding Views from Layout
        userIsOwner = user.getOwner();
        owner_button = (FloatingActionButton) findViewById(R.id.owner_button);
        Log.d("DEBUG","Owner: "+user.getOwner());
        if (userIsOwner) {
            owner_button.setVisibility(View.VISIBLE);
        }

        new PermissionChecker(MainMenu.this);

        codesListView = findViewById(R.id.QRCode_List_View);
        totalScanned = findViewById(R.id.num_scanned_text);
        totalScore = findViewById(R.id.total_score_text);
        setTexts();

        //Creating and Populating QRCodes listview
        codesDisplay = user.getCodesStrings();
        codesAdapter = new ArrayAdapter<String>(this, R.layout.qr_list, codesDisplay);
        codesListView.setAdapter(codesAdapter);

        //On Item Click to open QRCode page
        codesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int i, long l) {
                String selected =(String) (codesListView.getItemAtPosition(i));
                QRCode qrCode = user.getCode(user.getCodesStrings().indexOf(selected));
                Context context = getApplicationContext();
                Intent intent =new Intent(context, QrCodePage.class);
                intent.putExtra("QRCode",qrCode);
                intent.putExtra("User",user);
                intent.putExtra("currentUser", user);
                startActivity(intent);

            }
        });

        //Documentation for SwipeMenuListView
        //Github page: https://github.com/baoyongzhang/SwipeMenuListView
        //Youtube Video: https://www.youtube.com/watch?v=2aKL0qw4BVg
        //On Swipe for delete
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_trash_can);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        //Set creator
        codesListView.setMenuCreator(creator);

        codesListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //When Swipe Delete Button Is Pressed
                        QRCode code = user.getAllCodes().get(position);//Since both lists of codes should be indexed the same
                        user.removeQRCode(code);

                        //Deletes from User in firebase
                        deleteCode(user, code);

                        //Delete code from listview
                        codesDisplay.remove(position);
                        codesAdapter.notifyDataSetChanged();
                        setTexts();
                        break;
                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });
    }

    /**
     * Sets the total scanned and total score textviews to their current data
     */
    public void setTexts(){
        totalScanned.setText("Codes Scanned: " + Integer.toString(user.getAllCodes().size()));
        totalScore.setText("Total Score: "+ Integer.toString(user.getTotalScore()));
    }

    /**
     * Called when delete button hit, used to delete QR code from user's list
     * @param user
     *      User that code is being deleted from
     * @param qrCode
     *      Code to delete
     */
    public void deleteCode(User user, QRCode qrCode){
        try{
            final User oldUser = user;
            dataManager.removeCode(qrCode, new CallBack() {
                @Override
                public void onCall(User user) {
                    Log.d("TAG", "Delete QR Code"+ qrCode.getID());
                }
            });

        } catch(Exception e){
            Log.d("TAG", "QR DNE");
        }
    }

    /**
     * allows the action bar to be properly visible
     * @param menu
     *      the menu bar
     * @return
     *      success
     */
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
     * Go to owner page
     * @param view
     */
    public void onOwner(View view){
        Intent intent = new Intent(this, OwnerActivity.class);
        startActivity(intent);
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
                                intent.putExtra("User", user);
                                startActivity(intent);
                            }
                        }
                    }
                });
    }

    /**
     * Goes to the map activity
     * @param view
     *      View for the button
     */
    public void onMapClick(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    /**
     * Goes to the the search activity
     * @param view
     *      View for the button
     */
    public void onSearch(View view){
        Intent intent = new Intent(this, SearchQRPage.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    /**
     * https://developer.android.com/training/appbar/actions
     * @param item
     * @return
     *      Boolean for success
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