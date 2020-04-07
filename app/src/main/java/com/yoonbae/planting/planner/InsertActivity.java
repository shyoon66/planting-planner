package com.yoonbae.planting.planner;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class InsertActivity extends AppCompatActivity {
    private static final String TAG = "InsertActivity";
    private static final int REQUEST_ALBUM = 1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> requestPermissions());

        TextView adoptionDate = findViewById(R.id.adoptionDate);
        adoptionDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(InsertActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
                String monthStr = getMonthStr(monthOfYear + 1);
                String dayStr = getDayStr(dayOfMonth);
                String date = year + "-" + monthStr + "-" + dayStr;
                adoptionDate.setText(date);
            }, LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());

            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
            datePicker.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            datePickerDialog.show();
        });
    }

    private void requestPermissions() {
        Dexter.withActivity(InsertActivity.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            openAlbum();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show())
                .check();
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
}
