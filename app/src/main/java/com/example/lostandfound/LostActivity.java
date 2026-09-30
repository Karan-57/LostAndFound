package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import android.widget.PopupMenu;

public class LostActivity extends AppCompatActivity {

    LinearLayout tabFound;
    ImageButton btnSearch, btnAddLost, btnMenu;
    ListView lvLostItems;
    TextView tvEmptyLost;
    ItemAdapter itemAdapter;
    List<ItemModel> lostItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        tabFound = findViewById(R.id.tabFound);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddLost = findViewById(R.id.btnAddLost);
        btnMenu = findViewById(R.id.btnMenu);
        lvLostItems = findViewById(R.id.lvLostItems);
        tvEmptyLost = findViewById(R.id.tvEmptyLost);

        btnMenu.setOnClickListener(v -> showOverflowMenu(v));

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(LostActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        btnAddLost.setOnClickListener(v -> {
            Intent intent = new Intent(LostActivity.this, AddLostItemActivity.class);
            startActivity(intent);
        });

        tabFound.setOnClickListener(v -> {
            Intent intent = new Intent(LostActivity.this, FoundActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupListView();
    }

    private void setupListView() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        String campusCode = CampusManager.getJoinedCampusCode(this);
        lostItemList = dbHelper.getItemsByCampusAndType(campusCode, "lost");

        if (lostItemList.isEmpty()) {
            lvLostItems.setVisibility(View.GONE);
            tvEmptyLost.setVisibility(View.VISIBLE);
        } else {
            tvEmptyLost.setVisibility(View.GONE);
            lvLostItems.setVisibility(View.VISIBLE);
            itemAdapter = new ItemAdapter(this, lostItemList);
            lvLostItems.setAdapter(itemAdapter);
        }
    }

    private void showOverflowMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add(0, 1, 0, "My Uploaded Items");
        popup.getMenu().add(0, 2, 1, "Switch / Join Campus");
        popup.getMenu().add(0, 3, 2, "Logout");

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == 1) {
                Intent intent = new Intent(LostActivity.this, MyUploadsActivity.class);
                startActivity(intent);
                return true;
            } else if (id == 2) {
                CampusManager.leaveCampus(this);
                Intent intent = new Intent(LostActivity.this, JoinCampusActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (id == 3) {
                SessionManager.logout(this);
                CampusManager.leaveCampus(this);
                Intent intent = new Intent(LostActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
        popup.show();
    }
}
