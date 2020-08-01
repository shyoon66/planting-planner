package com.yoonbae.planting.planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yoonbae.planting.planner.adapter.MyRecyclerViewAdapter;
import com.yoonbae.planting.planner.viewmodel.PlantViewModel;

public class ListActivity extends AppCompatActivity {
    private PlantViewModel plantViewModel;

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
        RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
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
        return true;
    }
}