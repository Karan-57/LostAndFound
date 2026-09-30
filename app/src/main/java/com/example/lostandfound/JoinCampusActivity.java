package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JoinCampusActivity extends AppCompatActivity {

    private EditText etCampusCode;
    private Button btnJoinCampus;

    private EditText etNewCampusName, etNewCampusCode;
    private Button btnCreateCampus;

    private Button btnQuickStanford, btnQuickHarvard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_campus);

        etCampusCode = findViewById(R.id.etCampusCode);
        btnJoinCampus = findViewById(R.id.btnJoinCampus);

        etNewCampusName = findViewById(R.id.etNewCampusName);
        etNewCampusCode = findViewById(R.id.etNewCampusCode);
        btnCreateCampus = findViewById(R.id.btnCreateCampus);

        btnQuickStanford = findViewById(R.id.btnQuickStanford);
        btnQuickHarvard = findViewById(R.id.btnQuickHarvard);

        // Quick fills for testing demo campuses
        btnQuickStanford.setOnClickListener(v -> etCampusCode.setText("STAN2026"));
        btnQuickHarvard.setOnClickListener(v -> etCampusCode.setText("HARV2026"));

        // Join existing campus
        btnJoinCampus.setOnClickListener(v -> {
            String code = etCampusCode.getText().toString().trim().toUpperCase();
            if (code.isEmpty()) {
                etCampusCode.setError("Please enter a campus code");
                etCampusCode.requestFocus();
                return;
            }

            DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
            if (!dbHelper.campusExists(code)) {
                // Allow join anyway or inform user, but also auto-register code
                dbHelper.createCampus(code, code + " Campus");
            }

            CampusManager.setJoinedCampusCode(this, code);
            Toast.makeText(this, "Joined campus circle!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(JoinCampusActivity.this, FoundActivity.class);
            startActivity(intent);
            finish();
        });

        // Create new campus circle
        btnCreateCampus.setOnClickListener(v -> {
            String name = etNewCampusName.getText().toString().trim();
            String code = etNewCampusCode.getText().toString().trim().toUpperCase();

            if (name.isEmpty()) {
                etNewCampusName.setError("Enter campus name");
                etNewCampusName.requestFocus();
                return;
            }
            if (code.isEmpty() || code.length() < 3) {
                etNewCampusCode.setError("Code must be at least 3 characters");
                etNewCampusCode.requestFocus();
                return;
            }

            DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
            boolean created = dbHelper.createCampus(code, name);
            if (created) {
                CampusManager.setJoinedCampusCode(this, code);
                Toast.makeText(this, "Created and joined " + name + "!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(JoinCampusActivity.this, FoundActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Campus code already exists. Try joining it instead!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
