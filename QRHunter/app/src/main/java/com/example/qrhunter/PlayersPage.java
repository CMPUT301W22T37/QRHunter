package com.example.qrhunter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private User searchedUser;
    private User currentUser;

    /**
     * Function called when the activity is created
     * @param savedInstanceState
     *      Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_page);

        Intent intent = getIntent();
        allUsers = (ArrayList<User>) intent.getSerializableExtra("AllUsers");
        currentUser = (User) intent.getSerializableExtra("currentUser");

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
                    intent.putExtra("currentUser", currentUser);
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
            codesAdapter = new ArrayAdapter<String>(this, R.layout.qr_list, QRCodes);
            allQRCodesListView.setAdapter(codesAdapter);
        }
    }

    /**
     * Called when the search button is clicked
     * @param view
     *      View for the clicked button
     */
    public void onSearchClick(View view){
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

    public void setRankings(User user){
        int ranking;
        Collections.sort(allUsers, new UserComparatorTotalScanned());
        ranking = allUsers.indexOf(user);
        searchedTotalNumberRanking.setText(user.getUsername() + "'s Ranking for Number: "
                + ranking);

        Collections.sort(allUsers, new UserComparatorTotalSum());
        ranking = allUsers.indexOf(user);
        searchedTotalScoreRanking.setText(user.getUsername() + "'s Ranking For Sum: " +
                ranking);
    }


}