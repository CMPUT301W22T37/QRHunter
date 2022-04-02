package com.example.qrhunter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Activity for the page that allows players to look up other users and view their account
 */
public class PlayersPage extends AppCompatActivity {
    private ArrayList<User> allUsers;
    private EditText searchUsers;
    private TextView searchedHighestScoring;
    private TextView searchedTotalNumberRanking;
    private TextView searchedTotalScoreRanking;
    private TextView rankingTitle;
    private ListView allQRCodesListView;
    private ArrayList<String> QRCodes;
    private ArrayAdapter<String> codesAdapter;
    private User user;

    private final int SCAN_PROFILE_CODE = 1;

    private User searchedUser;


    /**
     * Function called when the activity is created
     * @param savedInstanceState
     *      Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("QRHunter");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        allUsers = (ArrayList<User>) intent.getSerializableExtra("AllUsers");
        user = (User) intent.getSerializableExtra("User");


        user = (User) intent.getSerializableExtra("User");


        //Find and set all views
        findViews();
        setViews(null);

        //OnClick Listener for comments ListView
        allQRCodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int i, long l) {
                String selected =(String) (allQRCodesListView.getItemAtPosition(i));
                if (searchedUser != null){
                    QRCode qrCode = searchedUser.getCode(searchedUser.getCodesStrings().indexOf(selected));
                    Context context = getApplicationContext();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Intent intent =new Intent(context, QrCodePage.class);
                    intent.putExtra("QRCode",qrCode);
                    intent.putExtra("User",searchedUser);
                    intent.putExtra("currentUser", user);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Finds all needed Views and Buttons
     */
    public void findViews(){
        searchUsers = findViewById(R.id.search_edit_text);
        searchedHighestScoring = findViewById(R.id.display_highest_QR_textview);
        searchedTotalNumberRanking = findViewById(R.id.display_total_ranking_textview);
        searchedTotalScoreRanking = findViewById(R.id.display_ranking_score_textview);
        rankingTitle = findViewById(R.id.ranking_title);
        allQRCodesListView = findViewById(R.id.qr_code_listview);

    }

    /**
     * Sets views to user's details, or blank if user is null
     * @param user
     *      User to be found from the arrayList, if null the views are set to blank
     */
    public void setViews(@Nullable User user){
        if (user == null){
            searchedHighestScoring.setText("");
            searchedTotalNumberRanking.setText("");
            searchedTotalScoreRanking.setText("");
            rankingTitle.setText("");
        }
        else {
            searchedUser = user;
            searchedHighestScoring.setText("Highest Scoring Code: " + user.getHighest());
            rankingTitle.setText(user.getUsername() + "'s Ranking");

            //Creating Listview for user's QRCodes
            QRCodes = user.getCodesStrings();
            codesAdapter = new ArrayAdapter<String>(this, R.layout.distance_list, QRCodes);
            allQRCodesListView.setAdapter(codesAdapter);
        }
    }

    /**
     * Called when the search button is clicked
     * @param view
     *      View for the clicked button
     */
    public void onSearchClick(View view){
        hideKeyboard(view);
        String searchedName = searchUsers.getText().toString();
        if (!searchedName.equals("")){//If there is something actually entered in search field
            User searchedUser = searchAllUsers(searchedName);
            if (searchedUser != null){
                setViews(searchedUser);
                setRankings(searchedUser);
            }
            else{
                Toast.makeText(getApplicationContext(), "User Does Not Exist", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "No User Entered", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Searches the ArrayList of all Users for a User with the given user name
     * @param userName
     *      User name of the User to find in the list
     * @return
     *      User with the given user name, null if user is not present
     */
    public User searchAllUsers(String userName){
        for (User user:allUsers) {
            if (user.getUsername().equals(userName)){
                return user;
            }
        }
        return null;
    }

    /**
     * searches players by comparing the username to the hash of the username
     * @param userhash
     *      the hash of the username we are looking for
     * @return
     *      the user (null if not found)
     */
    public User searchAllUsersByHash(String userhash){
        for (User user:allUsers) {
            if (QRCode.getHash(user.getUsername()).equals(userhash)){
                return user;
            }
        }
        return null;
    }


    public void setRankings(User user){
        int ranking;
        Collections.sort(allUsers, new UserComparatorTotalScanned());
        ranking = allUsers.indexOf(user) + 1;
        searchedTotalNumberRanking.setText(user.getUsername() + "'s Ranking for Number: "
                + ranking);

        Collections.sort(allUsers, new UserComparatorTotalSum());
        ranking = allUsers.indexOf(user) + 1;
        searchedTotalScoreRanking.setText(user.getUsername() + "'s Ranking For Sum: " +
                ranking);
    }

    /**
     * called when scanning a QR code to find a player is desirable
     * @param view
     *      the current view
     */
    public void onScan(View view){
        Intent intent = new Intent(this, ScanGameCodeActivity.class);
        startActivityForResult(intent,SCAN_PROFILE_CODE);
    }

    /**
     * gets result from the scanning activity
     * @param requestCode
     *      the code for the activity we are seeking the result from
     * @param resultCode
     *      the success code
     * @param data
     *      the data we are passing back
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SCAN_PROFILE_CODE) {
            if (resultCode == ScanGameCodeActivity.RESULT_OK) {
                String userhash = data.getStringExtra("userhash");
                User currentUser = searchAllUsersByHash(userhash);
                if(currentUser!=null){
                    setViews(currentUser);
                    setRankings(currentUser);
                }else{
                    Toast.makeText(getApplicationContext(), "User Does Not Exist", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                intent.putExtra("User",user);
                startActivity(intent);
                return true;
        }
        return true;
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
    }

}