package com.yoonbae.planting.planner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantDao;
import com.yoonbae.planting.planner.data.PlantDatabase;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewActivity extends AppCompatActivity {
    private static final String TAG = "ViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        long plantId = getPlantId(getIntent());
        if (plantId == 0) {
            return;
        }
        viewPlant(plantId);

        Button listBtn = findViewById(R.id.listBtn);
        listBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        });
    }

    private long getPlantId(Intent intent) {
        long id = intent.getLongExtra("id", 0);
        if (id == 0) {
            Toast.makeText(this, "식물정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            Intent listViewIntent = new Intent(ViewActivity.this, ListActivity.class);
            startActivity(listViewIntent);
            return 0;
        }
        return id;
    }

    private void viewPlant(Long plantId) {
        PlantDatabase plantDatabase = PlantDatabase.getDatabase(ViewActivity.this);
        PlantDao plantDao = plantDatabase.plantDao();
        plantDao.findById(plantId).observe(this, this::setPlant);
    }

    private void setPlant(Plant plant) {
        setImage(plant.getImagePath());
        setPlantName(plant.getName());
        setPlantDesc(plant.getDesc());
        setPlantAdoptionDate(plant.getAdoptionDate());
        setAlarm(plant.isAlarm());
        setAlarmDateTime(plant.getAlarmDateTime());
        setAlarmPeriod(plant.getAlarmPeriod());
    }

    private void setImage(String imagePath) {
        Uri imageUri = Uri.fromFile(new File(imagePath));
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(imageUri).into(imageView);
    }

    private void setPlantName(String plantName) {
        if (plantName == null) {
            return;
        }
        TextView plantNameTextView = findViewById(R.id.plantName);
        plantNameTextView.setText(plantName);
    }

    private void setPlantDesc(String desc) {
        if (desc == null) {
            return;
        }
        TextView plantDescTextView = findViewById(R.id.plantDesc);
        plantDescTextView.setText(desc);
    }

    private void setPlantAdoptionDate(LocalDate adoptionDate) {
        TextView adoptionDateTextView = findViewById(R.id.adoptionDate);
        adoptionDateTextView.setText(adoptionDate.format(DateTimeFormatter.ISO_DATE));
    }

    private void setAlarm(boolean isAlarm) {
        Switch alarm = findViewById(R.id.alarm);
        alarm.setChecked(isAlarm);
    }

    private void setAlarmDateTime(LocalDateTime alaramDateTime) {
        if (alaramDateTime == null) {
            return;
        }
        String alarmDate = alaramDateTime.format(DateTimeFormatter.ISO_DATE);
        setAlarmDate(alarmDate);
        String alarmTime = alaramDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        setAlarmTime(alarmTime);
    }

    private void setAlarmDate(String alarmDate) {
        TextView alarmDateTextView = findViewById(R.id.alarmDate);
        alarmDateTextView.setText(alarmDate);
    }

    private void setAlarmTime(String alarmTime) {
        TextView alarmDateTextView = findViewById(R.id.alarmTime);
        alarmDateTextView.setText(alarmTime);
    }

    @SuppressLint("SetTextI18n")
    private void setAlarmPeriod(int alarmPeriod) {
        TextView alarmPeriodTextView = findViewById(R.id.periodSpinner);
        alarmPeriodTextView.setText(alarmPeriod + "일");
    }
}
