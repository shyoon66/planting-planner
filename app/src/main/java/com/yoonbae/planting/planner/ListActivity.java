package com.yoonbae.planting.planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yoonbae.planting.planner.adapter.MyRecyclerViewAdapter;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantDao;
import com.yoonbae.planting.planner.data.PlantDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.yoonbae.planting.planner.data.PlantDatabase.databaseWriteExecutor;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            //actionBar.setIcon(R.drawable.baseline_add_black_24);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);    // 커스터마이징 하기 위해 필요
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);      // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
            //actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_arrow_left_black_24);
        }

        final RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.action_list);
        //BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.action_calendar:
                    intent = new Intent(ListActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }

            return false;
        });

        PlantDatabase plantDatabase = PlantDatabase.getDatabase(this);
        PlantDao plantDao = plantDatabase.plantDao();

        plantDao.findAll().observe(this, plants -> {
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
                break;
            case R.id.action_insert:
                intent = new Intent(ListActivity.this, InsertActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}