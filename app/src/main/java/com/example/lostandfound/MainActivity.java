package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        if (!SessionManager.isLoggedIn(this)) {
            // User not logged in -> redirect to LoginActivity
            intent = new Intent(MainActivity.this, LoginActivity.class);
        } else if (!CampusManager.hasJoinedCampus(this)) {
            // Logged in but needs to join a campus circle
            intent = new Intent(MainActivity.this, JoinCampusActivity.class);
        } else {
            // Logged in & has joined campus -> direct to main feed
            intent = new Intent(MainActivity.this, FoundActivity.class);
        }
        startActivity(intent);
        finish();
    }
}