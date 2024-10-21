package com.example.wordworrior;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button startButton;
    private Button leaderboardButton;
    private Button helpButton;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        startButton = findViewById(R.id.startButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);
        helpButton = findViewById(R.id.helpButton);
        welcomeTextView = findViewById(R.id.welcomeTextView);

        // Retrieve the username from the intent
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");

        // Set the welcome message
        if (userName != null) {
            welcomeTextView.setText("Welcome, " + userName + "!");
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the game (MainActivity)
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open LeaderboardActivity (implement this activity)
                Intent intent = new Intent(MenuActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open HelpActivity (implement this activity)
                Intent intent = new Intent(MenuActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
    }
}
