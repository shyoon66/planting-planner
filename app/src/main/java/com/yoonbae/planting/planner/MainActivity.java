package com.yoonbae.planting.planner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.yoonbae.planting.planner.adapter.ListViewAdapter;
import com.yoonbae.planting.planner.calendar.decorator.EventDecorator;
import com.yoonbae.planting.planner.calendar.decorator.HighlightWeekendsDecorator;
import com.yoonbae.planting.planner.calendar.decorator.OneDayDecorator;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantEvent;
import com.yoonbae.planting.planner.util.DateUtils;
import com.yoonbae.planting.planner.viewmodel.PlantViewModel;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener, AdapterView.OnItemClickListener  {
    private PlantViewModel plantViewModel;
    private MaterialCalendarView calendarView;
    private ListView listview;
    private List<Plant> plants = new ArrayList<>();
    private List<CalendarDay> waterAlarmDays = new ArrayList<>();
    private List<PlantEvent> plantEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        listview = findViewById(R.id.listview);
        initBottomNavigationView();
        initCalendar();
        initWaterAlarmDays();
    }

    private void initBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.action_list:
                    intent = new Intent(MainActivity.this, ListActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.action_settings:
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return false;
        });
    }

    private void initCalendar() {
        calendarView = findViewById(R.id.calendarView);
        calendarView.setDateSelected(CalendarDay.today(), true);
        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.months_array)));
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

    private void initWaterAlarmDays() {
        plantViewModel.findPlantsWithWateringAlarmSet().observe(this, plants -> {
            this.plants = plants;
            calendarEvent();
        });
    }

    private void calendarEvent() {
        onMonthChanged(calendarView, CalendarDay.today());
        onDateSelected(calendarView, CalendarDay.today(), true);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        waterAlarmDays.clear();
        plantEvents.clear();
        LocalDate firstDayOfTheMonth = DateUtils.getFirstDayOfTheMonth(date);
        LocalDate lastDayOfTheMonth = DateUtils.getLastDayOfTheMonth(date);
        for (Plant plant : plants) {
            addWaterAlarmDays(plant, firstDayOfTheMonth, lastDayOfTheMonth);
        }
        calendarView.addDecorator(new EventDecorator(Color.RED, waterAlarmDays, this));
    }

    private void addWaterAlarmDays(Plant plant, LocalDate firstDayOfTheMonth, LocalDate lastDayOfTheMonth) {
        LocalDate startAlarmDate = LocalDate.ofEpochDay(plant.getAlarmDate().toEpochDay());
        if (startAlarmDate.isAfter(lastDayOfTheMonth)) {
            return;
        }

        LocalDate alarmDate = LocalDate.ofEpochDay(startAlarmDate.toEpochDay());
        int alarmPeriod = plant.getAlarmPeriod();
        while (isNotAfterLastDayOfTheMonth(alarmDate, lastDayOfTheMonth)) {
            if (isValidAlarmDate(firstDayOfTheMonth, alarmDate)) {
                CalendarDay calendarDay = CalendarDay.from(alarmDate);
                waterAlarmDays.add(calendarDay);
                plantEvents.add(new PlantEvent(plant.getId(), plant.getName(), plant.getAlarmTime(), calendarDay));
            }
            alarmDate = alarmDate.plusDays(alarmPeriod);
        }
    }

    private boolean isNotAfterLastDayOfTheMonth(LocalDate alarmDate, LocalDate lastDayOfTheMonth) {
        return !alarmDate.isAfter(lastDayOfTheMonth);
    }

    private boolean isValidAlarmDate(LocalDate firstDayOfTheMonth, LocalDate alarmDate) {
        return alarmDate.isEqual(firstDayOfTheMonth) || alarmDate.isAfter(firstDayOfTheMonth);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        ListViewAdapter adapter = new ListViewAdapter();
        for (PlantEvent plantEvent : plantEvents) {
            if (date.equals(plantEvent.getCalendarDay())) {
                adapter.addItem(plantEvent);
            }
        }

        if (adapter.getCount() == 0) {
            listview.setAdapter(null);
        } else {
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlantEvent plantEvent = (PlantEvent) parent.getItemAtPosition(position);
        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
        intent.putExtra("id", plantEvent.getId());
        startActivity(intent);
        finish();
    }
}
