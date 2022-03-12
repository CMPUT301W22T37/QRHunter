package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Stats page to hold overall high scores and personal ranking details
 */

public class StatsPage extends AppCompatActivity {
    private TextView highestIndividualScore;
    private TextView lowestIndividualScore;
    private TextView highestGlobalScore;
    private TextView highestGlobalNumber;
    private ListView GlobalHighScores;
    private User user;

    /**
     * Called on creation of activity
     * @param savedInstanceState
     *      The instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_page);

        highestIndividualScore = findViewById(R.id.highest_score_textview);
        lowestIndividualScore = findViewById(R.id.lowest_score_textview);
        highestGlobalNumber = findViewById(R.id.total_ranking_number_textview);
        highestGlobalScore = findViewById(R.id.total_ranking_score_textview);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        setText();
    }

    /**
     * Sets the textViews to their proper value
     */
    public void setText(){
        highestIndividualScore.setText("Highest Score: "+ user.getHighest().toString());
        lowestIndividualScore.setText("Lowest Score: " + user.getLowest().toString());
    }
}