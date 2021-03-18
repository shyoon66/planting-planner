package com.yoonbae.planting.planner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yoonbae.planting.planner.adapter.MyRecyclerViewAdapter;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.viewmodel.PlantViewModel;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private PlantViewModel plantViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        initToolBar();
        initActionBar();
        initBottomNavigationView();
        initFloatingActionButton();
        settingPlantList();
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @SuppressLint("NonConstantResourceId")
    private void initBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.action_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.action_calendar:
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.action_settings:
                    intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return false;
        });
    }

    private void initFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, InsertActivity.class);
            startActivity(intent);
        });
    }

    private void settingPlantList() {
        recyclerView = findViewById(R.id.main_recyclerView);
        plantViewModel.findAll().observe(this, plants -> {
            recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
            MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(plants, ListActivity.this);
            recyclerView.setAdapter(myRecyclerViewAdapter);
            myRecyclerViewAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_list, menu);
        searchViewSettings(menu);
        return true;
    }

    private void searchViewSettings(Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("식물 이름을 입력하세요.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.equals("")) {
                    plantViewModel
                            .findAll()
                            .observe(ListActivity.this, this::recyclerViewSettings);
                } else {
                    plantViewModel
                            .findByName(newText)
                            .observe(ListActivity.this, this::recyclerViewSettings);
                }
                return false;
            }

            private void recyclerViewSettings(List<Plant> plants) {
                recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
                MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(plants, ListActivity.this);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                myRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
}