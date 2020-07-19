package com.yoonbae.planting.planner;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yoonbae.planting.planner.adapter.MyRecyclerViewAdapter;
import com.yoonbae.planting.planner.viewmodel.PlantViewModel;

public class ListActivity extends AppCompatActivity {
    private PlantViewModel plantViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initToolBar();
        initActionBar();
        initBottomNavigationView();
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        final RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
        settingPlantList(recyclerView);
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
                    intent = new Intent(ListActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.action_settings:
                    intent = new Intent(ListActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return false;
        });
    }

    private void settingPlantList(RecyclerView recyclerView) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_insert:
                intent = new Intent(ListActivity.this, InsertActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}