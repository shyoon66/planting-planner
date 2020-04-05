package com.yoonbae.planting.planner;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;

public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
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
