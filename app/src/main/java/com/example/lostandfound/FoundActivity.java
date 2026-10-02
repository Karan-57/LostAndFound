package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class FoundActivity extends AppCompatActivity {

    LinearLayout tabLost;
    ImageButton btnAddFound, btnSearch, btnMenu;
    ListView lvFoundItems;
    TextView tvEmptyFound;
    ItemAdapter itemAdapter;
    List<ItemModel> foundItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);

        tabLost = findViewById(R.id.tabLost);
        btnAddFound = findViewById(R.id.btnAddFound);
        btnSearch = findViewById(R.id.btnSearch);
        btnMenu = findViewById(R.id.btnMenu);
        lvFoundItems = findViewById(R.id.lvFoundItems);
        tvEmptyFound = findViewById(R.id.tvEmptyFound);

        btnMenu.setOnClickListener(this::showOverflowMenu);

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(FoundActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        tabLost.setOnClickListener(v -> {
            Intent intent = new Intent(FoundActivity.this, LostActivity.class);
            startActivity(intent);
            finish();
        });

        btnAddFound.setOnClickListener(v -> {
            Intent intent = new Intent(FoundActivity.this, AddFoundItemActivity.class);
            startActivity(intent);
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
        foundItemList = dbHelper.getItemsByCampusAndType(campusCode, "found");

        if (foundItemList.isEmpty()) {
            lvFoundItems.setVisibility(View.GONE);
            tvEmptyFound.setVisibility(View.VISIBLE);
        } else {
            tvEmptyFound.setVisibility(View.GONE);
            lvFoundItems.setVisibility(View.VISIBLE);
            itemAdapter = new ItemAdapter(this, foundItemList);
            lvFoundItems.setAdapter(itemAdapter);
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
                Intent intent = new Intent(FoundActivity.this, MyUploadsActivity.class);
                startActivity(intent);
                return true;
            } else if (id == 2) {
                CampusManager.leaveCampus(this);
                Intent intent = new Intent(FoundActivity.this, JoinCampusActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (id == 3) {
                SessionManager.logout(this);
                CampusManager.leaveCampus(this);
                Intent intent = new Intent(FoundActivity.this, LoginActivity.class);
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
