package com.yoonbae.planting.planner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.yoonbae.planting.planner.alarm.AlarmService;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.util.PermissionType;
import com.yoonbae.planting.planner.util.PermissionUtils;
import com.yoonbae.planting.planner.util.PlannerUtils;
import com.yoonbae.planting.planner.validator.PlantValidator;
import com.yoonbae.planting.planner.validator.Validator;
import com.yoonbae.planting.planner.viewmodel.PlantViewModel;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;

public class InsertActivity extends AppCompatActivity {
    private static final String TAG = "InsertActivity";
    private static final int REQUEST_ALBUM = 1;
    private ImageView imageView;
    private TextInputLayout plantNameLayOut;
    private TextInputLayout plantDescLayOut;
    private Uri imageUri = null;
    private TextView adoptionDate;
    private TextView alarmDateTextView;
    private TextView alarmTimeTextView;
    private Spinner periodSpinner;
    private Switch alarm;
    private Long plantId = 0L;
    private PlantViewModel plantViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        initComponent();
        initEvent();
        initPlant();
    }

    private void initComponent() {
        imageView = findViewById(R.id.imageView);
        plantNameLayOut = findViewById(R.id.plantName);
        plantDescLayOut = findViewById(R.id.plantDesc);
        adoptionDate = findViewById(R.id.adoptionDate);
        alarm = findViewById(R.id.alarm);
        alarmDateTextView = findViewById(R.id.alarmDate);
        alarmTimeTextView = findViewById(R.id.alarmTime);
        periodSpinner = findViewById(R.id.periodSpinner);
    }

    private void initEvent() {
        imageView.setOnClickListener(v -> {
            PermissionType permissionType = PermissionUtils.request(this);
            if (permissionType == PermissionType.GRANTED) {
                openAlbum();
            } else if (permissionType == PermissionType.DENIED) {
                showSettingsDialog();
            }
        });

        adoptionDate.setOnClickListener(v -> showDatePicker(adoptionDate));
        alarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setEnableAlarmItems(true);
            } else {
                setEnableAlarmItems(false);
            }
        });

        alarmDateTextView.setOnClickListener(v -> showDatePicker(alarmDateTextView));
        alarmTimeTextView.setOnClickListener(v -> showTimePicker());
        periodSpinner.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            ((TextView) periodSpinner.getSelectedView()).setTextColor(Color.rgb(121, 121, 121));
            ((TextView) periodSpinner.getSelectedView()).setTextSize(16);
            (periodSpinner.getSelectedView()).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.waterPeriod));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

            if (plantId > 0) {
                updatePlantAndSetWaterAlarm(plant);
            } else {
                savePlantAndSetWaterAlarm(plant);
            }
        });
    }

    private void initPlant() {
        Intent intent = getIntent();
        plantId = getPlantId(intent);
        if (plantId == 0) {
            return;
        }
        plantViewModel.findById(plantId).observe(this, plant -> {
            Uri imageUri = Uri.fromFile(new File(plant.getImagePath()));
            Glide.with(this).load(imageUri).into(imageView);

            EditText plantNameEditText = plantNameLayOut.getEditText();
            assert plantNameEditText != null;
            plantNameEditText.setText(plant.getName());

            EditText plantDescEditText = plantDescLayOut.getEditText();
            assert plantDescEditText != null;
            plantDescEditText.setText(plant.getDesc());

            LocalDate plantAdoptionDate = plant.getAdoptionDate();
            adoptionDate.setText(plantAdoptionDate.format(DateTimeFormatter.ISO_DATE));

            alarm.setChecked(plant.isAlarm());

            String alarmStartDt = plant.getAlarmDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            alarmDateTextView.setText(alarmStartDt);

            String alarmTime = plant.getAlarmTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            alarmTimeTextView.setText(alarmTime);

            Resources res = getResources();
            String[] waterPeriodArr = res.getStringArray(R.array.waterPeriod);
            String alarmPeriod = String.valueOf(plant.getAlarmPeriod());
            for(int i = 0; i < waterPeriodArr.length; i++) {
                if(waterPeriodArr[i].equals(alarmPeriod))
                    periodSpinner.setSelection(i);
            }
        });
    }

    private long getPlantId(Intent intent) {
        return intent.getLongExtra("id", 0);
    }

    private void setEnableAlarmItems(boolean b) {
        alarmDateTextView.setEnabled(b);
        alarmTimeTextView.setEnabled(b);
        periodSpinner.setEnabled(b);
    }

    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InsertActivity.this);
        builder.setTitle("권한 필요");
        builder.setMessage("이 기능을 사용하려면 이 앱에 권한이 필요합니다. 앱 설정에서 권한을 설정할 수 있습니다.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == REQUEST_ALBUM) {
            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri: " + photoUri);
            setImage(photoUri);
        }
    }

    private void setImage(Uri photoUri) {
        imageUri = photoUri;
        Glide.with(this).load(photoUri).into(imageView);
    }

    private String getMonthStr(int monthOfYear) {
        if (monthOfYear < 10) {
            return "0" + monthOfYear;
        }
        return String.valueOf(monthOfYear);
    }

    private String getDayStr(int dayOfMonth) {
        if (dayOfMonth < 10) {
            return "0" + dayOfMonth;
        }
        return String.valueOf(dayOfMonth);
    }

    private void showDatePicker(TextView dateView) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(InsertActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            String monthStr = getMonthStr(monthOfYear + 1);
            String dayStr = getDayStr(dayOfMonth);
            String date = year + "-" + monthStr + "-" + dayStr;
            dateView.setText(date);
        }, LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());

        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        int currentHour = LocalDateTime.now().getHour();
        int currentMinute = LocalDateTime.now().getMinute();

        TimePickerDialog timePickerDialog = new TimePickerDialog(InsertActivity.this, (timePicker, selectedHourOfDay, selectedMinute) -> {
            String hour = getDateStr(selectedHourOfDay);
            String minute = getDateStr(selectedMinute);
            String text = hour + ":" + minute;
            alarmTimeTextView.setText(text);
        }, currentHour, currentMinute, true);

        Window window = timePickerDialog.getWindow();
        if(window != null) {
            timePickerDialog.show();
        }
    }

    private String getDateStr(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return String.valueOf(value);
    }

    private Plant getPlant() throws IOException {
        Plant plant = new Plant();

        if (imageUri != null) {
            plant.setImagePath(PlannerUtils.getFilePathFromURI(getApplicationContext(), imageUri));
        }

        EditText plantNameEditText = plantNameLayOut.getEditText();
        String plantName = Optional.of(plantNameEditText.getText().toString()).orElse("");
        plant.setName(plantName);

        EditText plantDescEditText = plantDescLayOut.getEditText();
        String plantDesc = Optional.of(plantDescEditText.getText().toString()).orElse("");
        plant.setDesc(plantDesc);

        TextView adoptionDateTextView = findViewById(R.id.adoptionDate);
        String adoptionDate = Optional.of(adoptionDateTextView.getEditableText().toString()).orElse("");
        if (!adoptionDate.equals("날짜를 선택해주세요.")) {
            plant.setAdoptionDate(LocalDate.parse(adoptionDate, DateTimeFormatter.ISO_DATE));
        }

        plant.setAlarm(alarm.isChecked());
        if (plant.isAlarm()) {
            String alarmDate = Optional.of(alarmDateTextView.getEditableText().toString()).orElse("");
            String alarmTime = Optional.of(alarmTimeTextView.getEditableText().toString()).orElse("");
            if (!alarmDate.equals("날짜를 선택해주세요.") && !alarmTime.equals("시간을 선택해주세요.")) {
                LocalDateTime alarmDateTime = LocalDateTime.parse(alarmDate + " " + alarmTime + ":00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                plant.setAlarmDateTime(alarmDateTime);
            }

            String periodStr = Optional.of(periodSpinner.getSelectedItem().toString()).orElse("0일");
            int period = Integer.parseInt(periodStr.substring(0, periodStr.length() - 1));
            plant.setAlarmPeriod(period);
        } else {
            plant.setAlarmDateTime(null);
            plant.setAlarmPeriod(0);
        }

        return plant;
    }

    private void savePlantAndSetWaterAlarm(Plant plant) {
        plantViewModel.insert(plant);
        plantViewModel.findLatestPlantId().observe(this, latestPlantId -> {
            setWaterAlarm(plant, latestPlantId.intValue());
            Intent intent = new Intent(InsertActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }

    private void updatePlantAndSetWaterAlarm(Plant plant) {
        plantViewModel.update(plant);
        setWaterAlarm(plant, plant.getId().intValue());
    }

    private void setWaterAlarm(Plant plant, int plantId) {
        AlarmService.INSTANCE.cancelAlarm(getApplicationContext(), plantId);
        if (!plant.isAlarm()) {
            return;
        }
        String alarmDate = plant.getAlarmDateTime().format(DateTimeFormatter.ISO_DATE);
        String alarmTime = plant.getAlarmDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        long alarmTimeInMillis = getAlarmTimeInMillis(alarmDate, alarmTime, plant.getAlarmPeriod());
        long intervalMillis = plant.getAlarmPeriod() * 24 * 60 * 60 * 1000;
        AlarmService.INSTANCE.registeringAnAlarm(getApplicationContext(), alarmTimeInMillis, intervalMillis, plant.getName(), plantId);
    }

    private long getAlarmTimeInMillis(String alarmDate, String alarmTime, int alarmPeriod) {
        String[] alarmDateArr = alarmDate.split("-");
        int year = Integer.parseInt(alarmDateArr[0]);
        int month = Integer.parseInt(alarmDateArr[1]);
        int dayOfYear = Integer.parseInt(alarmDateArr[2]);
        String[] alarmTimeArr = alarmTime.split(":");
        int hourOfDay = Integer.parseInt(alarmTimeArr[0]);
        int minute = Integer.parseInt(alarmTimeArr[1]);

        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(year, month - 1, dayOfYear, hourOfDay, minute);
        long alarmTimeInMillis = alarmCalendar.getTimeInMillis();
        long nowTimeInMillis = Calendar.getInstance().getTimeInMillis();

        while(nowTimeInMillis > alarmTimeInMillis) {
            alarmCalendar.add(Calendar.DATE, alarmPeriod);
            alarmTimeInMillis = alarmCalendar.getTimeInMillis();
        }
        return alarmTimeInMillis;
    }
}
