package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Stats page to hold overall high scores and personal ranking details
 */

public class StatsPage extends AppCompatActivity {
    private TextView highestIndividualScore;
    private TextView lowestIndividualScore;
    private TextView highestGlobalScore;
    private TextView highestGlobalNumber;
    private ListView LeaderBoard;
    private TextView personalRanking;
    ArrayList<String> allUserNames;

    private User user;
    private FirebaseFirestore db;
    private Context context;
    private ArrayList<User> allUsers;

    /**
     * Called on creation of activity
     * @param savedInstanceState
     *      The instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("QRHunter");
        actionBar.setDisplayHomeAsUpEnabled(true);

        allUserNames = new ArrayList<>();
        context = getApplicationContext();
        db = FirebaseFirestore.getInstance();
        highestIndividualScore = findViewById(R.id.highest_score_textview);
        lowestIndividualScore = findViewById(R.id.lowest_score_textview);
        highestGlobalNumber = findViewById(R.id.total_ranking_number_textview);
        highestGlobalScore = findViewById(R.id.total_ranking_score_textview);
        LeaderBoard = findViewById(R.id.all_highscores_listview);
        personalRanking = findViewById(R.id.PersonalRankingTextView);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");
        allUsers = (ArrayList<User>) intent.getSerializableExtra("AllUsers");

        setText();
        setLeaderBoard(true, false);
    }

    /**
     * Sets the textViews to their current values
     */
    public void setText() {
        highestIndividualScore.setText("Highest Score: " + user.getHighest().toString());
        lowestIndividualScore.setText("Lowest Score: " + user.getLowest().toString());

        Collections.sort(allUsers, new UserComparatorTotalSum());
        int highestScore = allUsers.get(0).getTotalScore();

        Collections.sort(allUsers, new UserComparatorTotalScanned());
        int highestScanned = allUsers.get(0).getNumCodes();

        highestGlobalScore.setText("Highest Total Sum: " + highestScore);
        highestGlobalNumber.setText("Highest Number Scanned: " + highestScanned);
    }

    /**
     * Sets the leaderboard to the correct attribute selected
     * @param highScore
     *      Boolean stating whether to show high scores or not
     */
    public void setLeaderBoard(boolean highScore, boolean overall){
        //Sorting User based on given Attribute
        if (highScore){
            allUserNames.clear();
            Collections.sort(allUsers, new UserComparatorHighScore());
            for (User user:allUsers) {
                allUserNames.add(user.getUsername());
            }
            int number = allUserNames.indexOf(user.getUsername()) + 1;
            personalRanking.setText("Personal Ranking: " + number);

        }
        else if(overall){
            allUserNames.clear();
            Collections.sort(allUsers, new UserComparatorTotalSum());
            for (User user:allUsers) {
                allUserNames.add(user.getUsername());
            }
            int number = allUserNames.indexOf(user.getUsername()) + 1;
            personalRanking.setText("Personal Ranking: " + number);
        }
        else {
            allUserNames.clear();
            Collections.sort(allUsers, new UserComparatorTotalScanned());
            for (User user:allUsers) {
                allUserNames.add(user.getUsername());
            }
            int number = allUserNames.indexOf(user.getUsername()) + 1;
            personalRanking.setText("Personal Ranking: " + number);
        }

        //Populate and display the listView
        ArrayList<String> displayList = new ArrayList<>();
        if (highScore){
            for (User user:allUsers) {
            displayList.add(user.getUsername() + "'s High Score: " + user.getHighest());
            }
        }
        else if(overall){
            for (User user:allUsers) {
                displayList.add(user.getUsername() + "'s Total Score: " + user.getTotalScore());
            }
        }
        else {
            for (User user:allUsers) {
                displayList.add(user.getUsername() + "'s Total Scanned: " + user.getNumCodes());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.user_leaderboard, displayList);
        LeaderBoard.setAdapter(adapter);
    }

    /**
     * Sets the leaderboard for user's highest scoring QRCode
     * @param view
     *      View for the button
     */
    public void onHighScoresClick(View view){
        setLeaderBoard(true, false);
    }

    /**
     * Sets the leaderboard for user's total number of scanned QRCodes
     * @param view
     *      View for the button
     */
    public void onTotalScannedClick(View view){
        setLeaderBoard(false, false);
    }

    /**
     * Sets the leaderboard for the user's total sum of scanned QRCodes
     * @param view
     *      View for the button
     */
    public void onTotalSumClick(View view){
        setLeaderBoard(false, true);
    }

    /**
     * allows users to go back to the main menu
     * @param item
     *      the item on the action bar
     * @return
     *      success
     */
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
}