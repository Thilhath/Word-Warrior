package com.example.wordworrior;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 5000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Ensure you have a layout file for the splash screen
        // Find the ImageView by its ID
        ImageView gifImageView = findViewById(R.id.loading_gif);

        // Load the GIF using Glide
        Glide.with(this)
                .load(R.drawable.loading_gif) // Replace with your GIF file
                .into(gifImageView);

        // Handler to delay the transition to NameInputActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, NameInputActivity.class);
                startActivity(intent);
                finish(); // Finish SplashActivity so user can't go back to it
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}
