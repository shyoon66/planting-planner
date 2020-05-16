package com.yoonbae.planting.planner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.bumptech.glide.Glide;
import com.yoonbae.planting.planner.adapter.MyRecyclerViewAdapter;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantDao;
import com.yoonbae.planting.planner.data.PlantDatabase;

import java.io.File;

import static com.yoonbae.planting.planner.data.PlantDatabase.databaseWriteExecutor;

public class ViewActivity extends AppCompatActivity {
    private static final String TAG = "ViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        long plantId = getPlantId(getIntent());
        viewPlant(plantId);
    }

    private long getPlantId(Intent intent) {
        long id = intent.getLongExtra("id", 0);
        if (id == 0) {
            throw new IllegalArgumentException("식물정보를 가져오지 못했습니다.");
        }
        return id;
    }

    private void viewPlant(Long plantId) {
        databaseWriteExecutor.execute(() -> {
            PlantDatabase plantDatabase = PlantDatabase.getDatabase(ViewActivity.this);
            PlantDao plantDao = plantDatabase.plantDao();
            Plant plant = plantDao.findById(plantId).getValue();
            setPlant(plant);
        });
    }

    private void setPlant(Plant plant) {
        setImage(plant.getImagePath());
    }

    private void setImage(String imagePath) {
        Uri imageUri = Uri.fromFile(new File(imagePath));
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(imageUri).into(imageView);
    }
}
