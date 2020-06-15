package com.yoonbae.planting.planner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.yoonbae.planting.planner.calendar.decorator.HighlightWeekendsDecorator;
import com.yoonbae.planting.planner.calendar.decorator.OneDayDecorator;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantDao;
import com.yoonbae.planting.planner.data.PlantDatabase;

import org.threeten.bp.DayOfWeek;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        new Thread(() -> {
            calendarSetting();
            waterAlarmDatesSetting();
        });
    }

    private void calendarSetting() {
        calendarView = findViewById(R.id.calendarView);
        calendarView.setDateSelected(CalendarDay.today(), true);
        calendarView.state().edit()
                .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                .setMinimumDate(CalendarDay.from(2020, 1, 1))
                .setMaximumDate(CalendarDay.from(2050, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendarView.addDecorators(new HighlightWeekendsDecorator(), new OneDayDecorator());
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
    }

    private void waterAlarmDatesSetting() {
        PlantDatabase database = PlantDatabase.getDatabase(this);
        PlantDao plantDao = database.plantDao();
        plantDao.findPlantsWithWateringAlarmSet().observe(this, plants -> {
            for (Plant plant : plants) {
                calendarEvent();
            }
        });
    }

    private void calendarEvent() {
        onMonthChanged(calendarView, CalendarDay.today());
        //onDateSelected(calendarView, CalendarDay.today(), true);
    }

    private void waterAlarmDateSetting(Plant plant) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}
