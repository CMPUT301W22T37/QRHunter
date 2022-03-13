package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Class to represent a user's profile
 */
public class ProfilePage extends AppCompatActivity {
    User user;
    TextView userNameTextView;
    TextView emailTextView;

    /**
     * Called when the page is created
     * @param savedInstanceState
     *      Bundle representing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        userNameTextView = findViewById(R.id.username_textview);
        emailTextView = findViewById(R.id.email_textview);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        setText();


    }

    /**
     * Sets the text for the username and Email Address Textviews
     */
    public void setText(){
        userNameTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());
    }

    public void onClickGenerate(View view){
        Intent intent = new Intent(this, CreateQRCode.class);
        startActivity(intent);
    }
}