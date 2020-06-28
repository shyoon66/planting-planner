package com.yoonbae.planting.planner.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public enum AlarmService {
    INSTANCE;

    public void registeringAnAlarm(Context context, Long timeInMillis, Long intervalMillis, String name, int alarmId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("name", name);
        intent.putExtra("alarmId", alarmId);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, intervalMillis, sender);
    }

    public void cancelAlarm(Context context, int alarmId) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (sender != null) {
            assert am != null;
            am.cancel(sender);
            sender.cancel();
        }
    }
}
