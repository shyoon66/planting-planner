package com.yoonbae.planting.planner;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantDao;
import com.yoonbae.planting.planner.data.PlantDatabase;
import com.yoonbae.planting.planner.util.PlannerUtils;
import com.yoonbae.planting.planner.validator.PlantValidator;
import com.yoonbae.planting.planner.validator.Validator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.yoonbae.planting.planner.data.PlantDatabase.databaseWriteExecutor;

public class ModifyActivity extends InsertActivity {

    private Plant plant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Intent intent = getIntent();
        plant = (Plant)intent.getSerializableExtra("plant");

        init();
        onEditBtnClick();
        textInit();


    }

    private void onEditBtnClick(){
        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v -> {
            Plant plant;
            try {
                plant = getPlant();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "이미지 파일을 가져오는데 실패했습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            Validator validator = new PlantValidator();
            String validationMessage = validator.validate(plant);
            if (!validationMessage.equals("")) {
                Toast.makeText(getApplicationContext(), validator.validate(plant), Toast.LENGTH_LONG).show();
                return;
            }
            editPlant(plant);
        });
    }


    private void textInit(){

        ImageView imageView = findViewById(R.id.imageView);
        File file = new File(plant.getImagePath());
        if(file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bm);
        }
        imageUri = Uri.fromFile(file);

        TextInputLayout plantNameLayOut = findViewById(R.id.plantName);
        EditText plantNameEditText = plantNameLayOut.getEditText();
        plantNameEditText.setText(plant.getName());

        TextInputLayout plantDescLayOut = findViewById(R.id.plantDesc);
        EditText plantDescEditText = plantDescLayOut.getEditText();
        plantDescEditText.setText(plant.getDesc());

        Switch alarm = findViewById(R.id.alarm);
        alarm.setChecked(plant.isAlarm());
        alarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setEnableAlarmItems(true);
            } else {
                setEnableAlarmItems(true);
            }
        });

        TextView adoptionDateTextView = findViewById(R.id.adoptionDate);
        adoptionDateTextView.setText(plant.getAdoptionDate().toString());

        if(plant.getAlaramDateTime() != null) {

            String plantDateTime = plant.getAlaramDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String Date = plantDateTime.substring(0, 10);
            String Time = plantDateTime.substring(11, 16);

            alarmDateTextView.setText(Date);
            alarmTimeTextView.setText(Time);

            int plantPeriod = plant.getAlarmPeriod();
            String period = plantPeriod + "일";
            for(int i = 0; i < periodSpinner.getCount(); i++){
                if(periodSpinner.getItemAtPosition(i).toString().equals(period)){
                    periodSpinner.setSelection(i); break;
                }
            }
        }
    }

    private void editPlant(Plant p){
        databaseWriteExecutor.execute(() -> {
            PlantDatabase plantDatabase = PlantDatabase.getDatabase(ModifyActivity.this);
            PlantDao plantDao = plantDatabase.plantDao();
            plantDao.delete(plant);
            plantDao.insert(p);
            Intent intent = new Intent(ModifyActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }
}
