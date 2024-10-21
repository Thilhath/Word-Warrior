package com.example.wordworrior;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NameInputActivity extends AppCompatActivity {

    private EditText nameInput;
    private Button submitButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_input);

        nameInput = findViewById(R.id.nameInput);
        submitButton = findViewById(R.id.submitButton);

        // Initialize shared preferences to save the user's name
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Check if a name is already saved
        String savedName = sharedPreferences.getString("userName", null);
        if (savedName != null) {
            navigateToMenu(savedName);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = nameInput.getText().toString();
                if (!userName.isEmpty()) {
                    // Save the name in shared preferences
                    sharedPreferences.edit().putString("userName", userName).apply();
                    navigateToMenu(userName);
                } else {
                    Toast.makeText(NameInputActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navigateToMenu(String userName) {
        Intent intent = new Intent(NameInputActivity.this, MenuActivity.class);
        intent.putExtra("userName", userName); // Pass the username to MenuActivity
        startActivity(intent);
        finish(); // Close this activity
    }

}
