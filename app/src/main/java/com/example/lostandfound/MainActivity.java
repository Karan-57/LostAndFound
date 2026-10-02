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
            intent = new Intent(MainActivity.this, LoginActivity.class);
        } else if (!CampusManager.hasJoinedCampus(this)) {
            intent = new Intent(MainActivity.this, JoinCampusActivity.class);
        } else {
            intent = new Intent(MainActivity.this, FoundActivity.class);
        }
        startActivity(intent);
        finish();
    }
}