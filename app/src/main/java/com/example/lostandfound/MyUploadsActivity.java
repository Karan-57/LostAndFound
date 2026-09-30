package com.example.lostandfound;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MyUploadsActivity extends AppCompatActivity {

    private ImageButton btnMyUploadsBack;
    private TextView tvMyUploadsUserEmail;
    private TextView tvMyUploadsCount;
    private ListView lvMyUploads;
    private TextView tvMyUploadsEmpty;
    private ItemAdapter adapter;
    private List<ItemModel> myItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploads);

        btnMyUploadsBack = findViewById(R.id.btnMyUploadsBack);
        tvMyUploadsUserEmail = findViewById(R.id.tvMyUploadsUserEmail);
        tvMyUploadsCount = findViewById(R.id.tvMyUploadsCount);
        lvMyUploads = findViewById(R.id.lvMyUploads);
        tvMyUploadsEmpty = findViewById(R.id.tvMyUploadsEmpty);

        btnMyUploadsBack.setOnClickListener(v -> finish());

        loadMyUploads();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyUploads();
    }

    private void loadMyUploads() {
        String email = SessionManager.getUserEmail(this);
        if (email.isEmpty()) {
            email = "test@test.com"; // Default to logged in email or fallback
        }

        tvMyUploadsUserEmail.setText("Account: " + email);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        myItemsList = dbHelper.getItemsByUserEmail(email);

        int count = myItemsList.size();
        tvMyUploadsCount.setText(count + (count == 1 ? " item uploaded" : " items uploaded"));

        if (myItemsList.isEmpty()) {
            lvMyUploads.setVisibility(View.GONE);
            tvMyUploadsEmpty.setVisibility(View.VISIBLE);
        } else {
            tvMyUploadsEmpty.setVisibility(View.GONE);
            lvMyUploads.setVisibility(View.VISIBLE);
            adapter = new ItemAdapter(this, myItemsList);
            adapter.setShowResolveButton(true, item -> {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Mark as Found / Resolved")
                        .setMessage("Are you sure this item has been resolved? It will be permanently removed from all listings.")
                        .setPositiveButton("Mark Resolved", (dialog, which) -> {
                            boolean deleted = dbHelper.deleteItem(item.getId());
                            if (deleted) {
                                android.widget.Toast.makeText(this, "Item marked as resolved and removed!", android.widget.Toast.LENGTH_SHORT).show();
                                loadMyUploads();
                            } else {
                                android.widget.Toast.makeText(this, "Failed to remove item", android.widget.Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
            lvMyUploads.setAdapter(adapter);
        }
    }
}
