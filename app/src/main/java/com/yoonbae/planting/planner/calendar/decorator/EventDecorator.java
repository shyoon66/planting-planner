package com.yoonbae.planting.planner.calendar.decorator;

import android.app.Activity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {
    private int color;
    private HashSet<CalendarDay> dates;
    //private Drawable drawable;

    public EventDecorator(int color, Collection<CalendarDay> dates, Activity context) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        //this.drawable = context.getResources().getDrawable(com.prolificinteractive.materialcalendarview.R.drawable.abc_ab_share_pack_mtrl_alpha);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color));
        //view.setSelectionDrawable(drawable);
    }
}
