package com.yoonbae.planting.planner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantDao;
import com.yoonbae.planting.planner.data.PlantDatabase;
import com.yoonbae.planting.planner.util.PermissionType;
import com.yoonbae.planting.planner.util.PermissionUtils;
import com.yoonbae.planting.planner.validator.PlantValidator;
import com.yoonbae.planting.planner.validator.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;

import static com.yoonbae.planting.planner.data.PlantDatabase.databaseWriteExecutor;

public class InsertActivity extends AppCompatActivity {
    private static final String TAG = "InsertActivity";
    private static final int REQUEST_ALBUM = 1;
    private ImageView imageView;
    private Uri imageUri = null;
    private TextView adoptionDate;
    private TextView alarmDateTextView;
    private TextView alarmTimeTextView;
    private Spinner periodSpinner;
    private Switch alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> {
            PermissionType permissionType = PermissionUtils.request(this);
            if (permissionType == PermissionType.GRANTED) {
                openAlbum();
            } else if (permissionType == PermissionType.DENIED) {
                showSettingsDialog();
            }
        });

        adoptionDate = findViewById(R.id.adoptionDate);
        adoptionDate.setOnClickListener(v -> showDatePicker(adoptionDate));

        alarm = findViewById(R.id.alarm);
        alarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setEnableAlarmItems(true);
            } else {
                setEnableAlarmItems(false);
            }
        });

        alarmDateTextView = findViewById(R.id.alarmDate);
        alarmDateTextView.setOnClickListener(v -> showDatePicker(alarmDateTextView));

        alarmTimeTextView = findViewById(R.id.alarmTime);
        alarmTimeTextView.setOnClickListener(v -> showTimePicker());

        periodSpinner = findViewById(R.id.periodSpinner);
        periodSpinner.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            ((TextView) periodSpinner.getSelectedView()).setTextColor(Color.rgb(121, 121, 121));
            ((TextView) periodSpinner.getSelectedView()).setTextSize(16);
            (periodSpinner.getSelectedView()).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.waterPeriod));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v -> {
            Plant plant = getPlant();
            Validator validator = new PlantValidator();
            String validationMessage = validator.validate(plant);
            if (!validationMessage.equals("")) {
                Toast.makeText(getApplicationContext(), validator.validate(plant), Toast.LENGTH_LONG).show();
                return;
            }
            savePlant(plant);
        });
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
        datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
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

    private Plant getPlant() {
        Plant plant = new Plant();

        if (imageUri != null) {
            plant.setImagePath(imageUri.getPath());
        }

        TextInputLayout plantNameLayOut = findViewById(R.id.plantName);
        EditText plantNameEditText = plantNameLayOut.getEditText();
        String plantName = Optional.of(plantNameEditText.getText().toString()).orElse("");
        plant.setName(plantName);

        TextInputLayout plantDescLayOut = findViewById(R.id.plantDesc);
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
                plant.setAlaramDateTime(alarmDateTime);
            }

            String periodStr = Optional.of(periodSpinner.getSelectedItem().toString()).orElse("0일");
            int period = Integer.parseInt(periodStr.substring(0, periodStr.length() - 1));
            plant.setAlarmPeriod(period);
        } else {
            plant.setAlaramDateTime(null);
            plant.setAlarmPeriod(0);
        }

        return plant;
    }

    public void savePlant(Plant plant) {
        databaseWriteExecutor.execute(() -> {
            PlantDatabase plantDatabase = PlantDatabase.getDatabase(InsertActivity.this);
            PlantDao plantDao = plantDatabase.plantDao();
            plantDao.insert(plant);
            Intent intent = new Intent(InsertActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }
}
