package com.example.movieapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.example.movieapp.ui.auth.LoginActivity;
import com.example.movieapp.utils.SharedPrefsHelper;

/**
 * Splash Activity
 * Shows splash screen and navigates to appropriate screen based on login status
 */
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2 seconds
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Navigate after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance(this);
            
            Intent intent;
            if (sharedPrefsHelper.isLoggedIn()) {
                // User is logged in, go to MainActivity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // User is not logged in, go to LoginActivity
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}

