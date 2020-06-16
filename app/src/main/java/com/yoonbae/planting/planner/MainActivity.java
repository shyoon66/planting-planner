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
import com.yoonbae.planting.planner.util.DateUtils;

import org.threeten.bp.DayOfWeek;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.yoonbae.planting.planner.data.PlantDatabase.databaseWriteExecutor;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    private MaterialCalendarView calendarView;
    private List<Plant> plants;

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
        calendarSetting();
        waterAlarmDatesSetting();
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
        databaseWriteExecutor.execute(() -> {
            PlantDatabase database = PlantDatabase.getDatabase(this);
            PlantDao plantDao = database.plantDao();
            plants = Optional.ofNullable(plantDao.findPlantsWithWateringAlarmSet()).orElseGet(ArrayList::new);
            calendarEvent();
        });
    }

    private void calendarEvent() {
        onMonthChanged(calendarView, CalendarDay.today());
        //onDateSelected(calendarView, CalendarDay.today(), true);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        LocalDate firstDayOfTheMonth = DateUtils.convert2LocalDate(date);
        LocalDate lastDayOfTheMonth = DateUtils.getLastDayOfTheMonth(date);
        for (Plant plant : plants) {
            addEventDays(plant, firstDayOfTheMonth, lastDayOfTheMonth);
        }
    }

    private void addEventDays(Plant plant, LocalDate firstDayOfTheMonth, LocalDate lastDayOfTheMonth) {
        LocalDate alarmStartDate = plant.getAlarmDate();
        if (alarmStartDate.isAfter(lastDayOfTheMonth)) {
            return;
        }
    }
}
