package com.example.wordworrior;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView helpTextView = findViewById(R.id.helpText );
        String rules = "Game Rules:\n\n" +
                "1. Objective: Guess the correct word.\n\n" +
                "2. How to Play: You will see blank spaces representing the letters in the word. Enter your guess using the on-screen keyboard.\n\n" +
                "3. Scoring: Points will get deducted for each incorrect guess. Try to guess the word in the fewest attempts for a higher score.\n\n" +
                "4. End of Game: The game ends when the word is correctly guessed or when you run out of attempts. Your score will be displayed at the end of each round.\n\n" +
                "5. You can request the number of letters and occurrence of a particular letter  in the word at cost of five points.\n\n"+
                "6. Start a New Game: Press the Play Now button to start a new game anytime.\n\n " +
                "7. After 5th attempt you can get a tip about the word tip can be the letters that are not in the word. ";

        helpTextView.setText(rules);
    }
}
