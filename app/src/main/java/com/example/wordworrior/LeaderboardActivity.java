package com.example.wordworrior;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class LeaderboardActivity extends AppCompatActivity {

    private TextView leaderboardView;
    private static final String TAG = "LeaderboardActivity"; // For logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize TextView to display the leaderboard
        leaderboardView = findViewById(R.id.leaderboardView);

        // Fetch and display the leaderboard data
        fetchLeaderboard();
    }

    // Method to fetch leaderboard data from Dreamlo
    private void fetchLeaderboard() {
        String dreamloURL = "http://dreamlo.com/lb/67136bd58f40bc122c27743b/json";  // Use Dreamlo 'json' format

        // Initialize OkHttpClient for the network request
        OkHttpClient client = new OkHttpClient();

        // Build the request to the Dreamlo API
        Request request = new Request.Builder().url(dreamloURL).build();

        // Asynchronous network request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Show a toast message if the network request fails
                runOnUiThread(() -> Toast.makeText(LeaderboardActivity.this, "Failed to fetch leaderboard!", Toast.LENGTH_SHORT).show());
                Log.e(TAG, "Network request failed: " + e.getMessage()); // Log the error
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse the response data
                    final String responseData = response.body().string();
                    Log.d(TAG, "Response data: " + responseData); // Log the response
                    runOnUiThread(() -> displayLeaderboard(responseData));  // Update UI on the main thread
                } else {
                    // Show a toast message if the response is not successful
                    runOnUiThread(() -> Toast.makeText(LeaderboardActivity.this, "Failed to load leaderboard!", Toast.LENGTH_SHORT).show());
                    Log.e(TAG, "Unsuccessful response");
                }
            }
        });
    }

    // Method to parse and display the leaderboard from JSON
    private void displayLeaderboard(String data) {
        StringBuilder leaderboardText = new StringBuilder();

        try {
            // Parse the JSON response using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(data);
            JsonNode leaderboardEntries = rootNode.path("dreamlo").path("leaderboard").path("entry");

            // Loop through each leaderboard entry
            for (JsonNode entry : leaderboardEntries) {
                String playerName = entry.path("name").asText();  // Player name
                String score = entry.path("score").asText();      // Player score

                // Append the player info to the leaderboard text
                leaderboardText.append("Player: ").append(playerName)
                        .append(" - Score: ").append(score)
                        .append("\n"); // Add a newline for better readability
            }

        } catch (Exception e) {
            e.printStackTrace();
            leaderboardText.append("Failed to parse leaderboard data.");
            Log.e(TAG, "JSON parsing error: " + e.getMessage()); // Log parsing error
        }

        // Display the formatted leaderboard in the TextView
        leaderboardView.setText(leaderboardText.toString());
    }
}