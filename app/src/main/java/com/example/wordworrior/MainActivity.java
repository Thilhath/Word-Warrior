package com.example.wordworrior;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView scoreView, timerView, resultView, attemptsView, wordLengthView;
    private EditText guessInput;
    private Button guessButton, letterButton, tipButton, lengthButton;

    private String secretWord;
    private int score = 100;
    private int attempts = 0;
    private int timerSeconds = 0;
    private boolean gameRunning = false;
    private boolean tipUsed = false; // To track if the tip has been used

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (gameRunning) {
                timerSeconds++;
                int minutes = timerSeconds / 60;
                int seconds = timerSeconds % 60;
                timerView.setText(String.format("Time: %02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    private SharedPreferences sharedPreferences; // To store preferences
    private String userName; // To store the username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        scoreView = findViewById(R.id.scoreView);
        timerView = findViewById(R.id.timerView);
        resultView = findViewById(R.id.resultView);
        attemptsView = findViewById(R.id.attemptsView);
        wordLengthView = findViewById(R.id.wordLengthView);
        guessInput = findViewById(R.id.guessInput);
        guessButton = findViewById(R.id.guessButton);
        letterButton = findViewById(R.id.letterButton);
        tipButton = findViewById(R.id.tipButton);
        lengthButton = findViewById(R.id.lengthButton);

        // Initialize SharedPreferences to get the username
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Get the saved username
        userName = sharedPreferences.getString("userName", "Guest"); // Default to 'Guest' if no name is found

        // Start a new game
        startNewGame();

        // Set up Guess button
        guessButton.setOnClickListener(v -> {
            String guess = guessInput.getText().toString().trim().toLowerCase();
            if (guess.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a guess!", Toast.LENGTH_SHORT).show();
                return;
            }
            attempts++;
            attemptsView.setText("Attempts: " + attempts);
            if (guess.equals(secretWord)) {
                gameRunning = false;
                showEndGamePopup(true);
                sendScoreToLeaderboard(userName, score); // Send score after winning
            } else {
                score -= 10;
                scoreView.setText("Score: " + score);
                if (score <= 0) {
                    gameRunning = false;
                    showEndGamePopup(false);
                } else {
                    resultView.setText("Incorrect guess, try again!");
                }
            }
        });

        // Set up Length button
        lengthButton.setOnClickListener(v -> {
            score -= 5;
            scoreView.setText("Score: " + score);
            wordLengthView.setText("Word Length: " + secretWord.length());

            Toast.makeText(MainActivity.this, "Please :  "+secretWord, Toast.LENGTH_LONG).show();
        });

        // Set up Letter button
        letterButton.setOnClickListener(v -> {
            String letter = guessInput.getText().toString().trim().toLowerCase();
            if (letter.length() != 1) {
                Toast.makeText(MainActivity.this, "Please enter a single letter!", Toast.LENGTH_SHORT).show();
                return;
            }
            score -= 5;
            scoreView.setText("Score: " + score);
            int occurrences = countLetterOccurrences(letter.charAt(0));
            resultView.setText("Letter '" + letter + "' occurs " + occurrences + " times.");
        });

        // Set up Tip button
        tipButton.setOnClickListener(v -> {
            if (attempts < 5) {
                Toast.makeText(MainActivity.this, "Tips are available after 5 attempts!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tipUsed) {
                Toast.makeText(MainActivity.this, "You've already used your tip!", Toast.LENGTH_SHORT).show();
                return;
            }
            tipUsed = true; // Mark that a tip has been used
            provideTip();
        });
    }

    // Start a new game
    private void startNewGame() {
        fetchRandomWord();
        score = 100;
        attempts = 0;
        timerSeconds = 0;
        gameRunning = true;
        guessInput.setText("");
        scoreView.setText("Score: " + score);
        attemptsView.setText("Attempts: " + attempts);
        resultView.setText("");
        wordLengthView.setText("Word Length: ?");
        timerView.setText("Time: 00:00");
        timerHandler.postDelayed(timerRunnable, 1000);
        tipUsed = false; // Reset tip usage for a new game
    }

    // Show end game popup
    private void showEndGamePopup(boolean won) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        if (won) {
            builder.setTitle("Congratulations!");
            builder.setMessage("You guessed the word!\nScore: " + score + "\nTime: " + timerSeconds + " seconds\nAttempts: " + attempts);
        } else {
            builder.setTitle("Game Over");
            builder.setMessage("You ran out of points. The word was: '" + secretWord + "'. Try again?");
        }
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startNewGame();
            }
        });
        builder.show();
    }

    // Send score to Dreamlo leaderboard
    private void sendScoreToLeaderboard(String playerName, int score) {
        String dreamloURL = "http://dreamlo.com/lb/o8GN2aAwWkGp754jZgd2IQEecmwXAZ9k24nyBF_i0Y9A/add/" + playerName + "/" + score+"/"+timerSeconds;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(dreamloURL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to add score to leaderboard!", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Score added to leaderboard!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    // Fetch a random word from the API
    private void fetchRandomWord() {
        String url = "https://random-word-api.herokuapp.com/word?number=1";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch word!", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    secretWord = responseData.replaceAll("[\\[\\]\"]", ""); // Clean up the response
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Word fetched!", Toast.LENGTH_LONG).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch word!", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // Count occurrences of a letter in the secret word
    private int countLetterOccurrences(char letter) {
        int count = 0;
        for (char c : secretWord.toCharArray()) {
            if (c == letter) {
                count++;
            }
        }
        return count;
    }

    // Provide a tip to the player
    private void provideTip() {
        StringBuilder notInWord = new StringBuilder();
        for (char c = 'a'; c <= 'z'; c++) {
            if (!secretWord.contains(String.valueOf(c))) {
                notInWord.append(c).append(", ");
            }
        }

        // Remove trailing comma and space
        if (notInWord.length() > 0) {
            notInWord.setLength(notInWord.length() - 2);
        }

        String tipMessage = "Letters not in the word: " + notInWord.toString() + "\n";
        tipMessage += "Tip: The word starts with '" + secretWord.charAt(0) + "' and ends with '" + secretWord.charAt(secretWord.length() - 1) + "'.";

        resultView.setText(tipMessage);
    }
}