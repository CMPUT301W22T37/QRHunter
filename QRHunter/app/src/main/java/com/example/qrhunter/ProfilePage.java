package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProfilePage extends AppCompatActivity {
    User user;
    TextView userNameTextView;
    TextView emailTextView;

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
}