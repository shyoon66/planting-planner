package com.yoonbae.planting.planner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.yoonbae.planting.planner.alarm.AlarmService;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.util.DateUtils;
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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class InsertActivity extends AppCompatActivity {
    private static final String TAG = "InsertActivity";
    private static final int REQUEST_CAMERA = 0;
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
    private Integer plantId = 0;
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
                showImageDialog();
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.water_period));
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
                plant.setId(plantId);
                updatePlantAndSetWaterAlarm(plant);
            } else {
                try {
                    savePlantAndSetWaterAlarm(plant);
                } catch (ExecutionException | InterruptedException e) {
                    if (e instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                    Toast.makeText(getApplicationContext(), "식물 등록이 실패했습니다.\n잠시 후에 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                    Log.w(TAG, e);
                }
            }
        });
    }

    private void showImageDialog() {
        final String[] items = {"카메라로 찍기", "앨범에서 가져오기", "취소"};
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("사진 선택");
        ab.setItems(items, (dialog, index) -> {
            if (index == 0) {
                startCamera();
            } else if(index == 1) {
                openAlbum();
            }
            dialog.dismiss();
        });
        ab.show();
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = createImageFile();
            Uri photoUri = FileProvider.getUriForFile(
                    this, "com.yoonbae.planting.planner", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            imageUri = photoUri;
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch(IOException e) {
            Toast.makeText(
        this, "카메라 실행중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            Log.e("startCamera IOException", Objects.requireNonNull(e.getMessage()));
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null || (!storageDir.exists() && !storageDir.mkdirs())) {
            Toast.makeText(
        this, "이미지를 저장할 폴더를 생성하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void initPlant() {
        Intent intent = getIntent();
        plantId = getPlantId(intent);
        if (plantId == 0) {
            return;
        }

        plantViewModel.findById(plantId).observe(this, plant -> {
            Uri imageUri = Uri.fromFile(new File(plant.getImagePath()));
            this.imageUri = imageUri;
            Glide.with(this).load(imageUri).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

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
            String[] waterPeriodArr = res.getStringArray(R.array.water_period);
            int alarmPeriod = plant.getAlarmPeriod();
            for (int i = 0; i < waterPeriodArr.length; i++) {
                int waterPeriod = Integer.parseInt(waterPeriodArr[i].substring(0, waterPeriodArr[i].length() - 1));
                if (alarmPeriod == waterPeriod) {
                    periodSpinner.setSelection(i);
                }
            }
        });
    }

    private Integer getPlantId(Intent intent) {
        return intent.getIntExtra("id", 0);
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
        if (requestCode == REQUEST_CAMERA) {
            setImage(imageUri);
        } else if (requestCode == REQUEST_ALBUM) {
            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri: " + photoUri);
            imageUri = photoUri;
            setImage(photoUri);
        }
    }

    private void setImage(Uri photoUri) {
        Glide.with(this).load(photoUri).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

    private void savePlantAndSetWaterAlarm(Plant plant) throws ExecutionException, InterruptedException {
        Future<Long> insertFuture = plantViewModel.insert(plant);
        int latestPlantId = insertFuture.get().intValue();
        setWaterAlarm(plant, latestPlantId);
        Intent intent = new Intent(InsertActivity.this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    private void updatePlantAndSetWaterAlarm(Plant plant) {
        plantViewModel.update(plant);
        setWaterAlarm(plant, plantId);
        Intent intent = new Intent(InsertActivity.this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    private void setWaterAlarm(Plant plant, int plantId) {
        AlarmService.INSTANCE.cancelAlarm(getApplicationContext(), plantId);
        if (!plant.isAlarm()) {
            return;
        }
        long alarmTimeInMillis = DateUtils.getAlarmTimeInMillis(plant.getAlarmDateTime(), plant.getAlarmPeriod());
        long intervalMillis = DateUtils.getAlarmPeriodInterval(plant.getAlarmPeriod());
        AlarmService.INSTANCE.registeringAnAlarm(getApplicationContext(), alarmTimeInMillis, intervalMillis, plant.getName(), plantId);
    }
}
