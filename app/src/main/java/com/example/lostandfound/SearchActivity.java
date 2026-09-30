package com.example.lostandfound;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ImageButton btnSearchBack;
    EditText etSearchInput;
    ListView lvSearchResults;
    TextView tvSearchEmpty;
    ItemAdapter searchAdapter;
    List<ItemModel> allItemsList;
    List<ItemModel> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnSearchBack = findViewById(R.id.btnSearchBack);
        etSearchInput = findViewById(R.id.etSearchInput);
        lvSearchResults = findViewById(R.id.lvSearchResults);
        tvSearchEmpty = findViewById(R.id.tvSearchEmpty);

        btnSearchBack.setOnClickListener(v -> finish());

        loadAllItems();

        filteredList = new ArrayList<>(allItemsList);
        searchAdapter = new ItemAdapter(this, filteredList);
        lvSearchResults.setAdapter(searchAdapter);

        updateEmptyState();

        etSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                filterItems(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterItems(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(allItemsList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (ItemModel item : allItemsList) {
                if (item.getDescription().toLowerCase().contains(lowerQuery)
                        || item.getLocation().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(item);
                }
            }
        }
        searchAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void loadAllItems() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        String campusCode = CampusManager.getJoinedCampusCode(this);
        allItemsList = dbHelper.getAllItemsByCampus(campusCode);
    }

    private void updateEmptyState() {
        if (filteredList.isEmpty()) {
            lvSearchResults.setVisibility(View.GONE);
            tvSearchEmpty.setVisibility(View.VISIBLE);
        } else {
            tvSearchEmpty.setVisibility(View.GONE);
            lvSearchResults.setVisibility(View.VISIBLE);
        }
    }
}
