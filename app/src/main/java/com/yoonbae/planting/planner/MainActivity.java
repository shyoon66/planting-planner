package com.yoonbae.planting.planner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.yoonbae.planting.planner.calendar.decorator.HighlightWeekendsDecorator;
import com.yoonbae.planting.planner.calendar.decorator.OneDayDecorator;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.calendarView);
        calendarSetting();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.action_list:
                    intent = new Intent(MainActivity.this, ListActivity.class);
                    startActivity(intent);
                    break;
            }

            return false;
        });
    }

    private void calendarSetting() {
        calendarView.setDateSelected(CalendarDay.today(), true);
        calendarView.addDecorators(new HighlightWeekendsDecorator(), new OneDayDecorator());
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}
