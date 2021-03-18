package com.yoonbae.planting.planner.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.yoonbae.planting.planner.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String NOTIFICATION_CHANNEL_WATER_ALARM_ID = "1";
    private static final String NOTIFICATION_CHANNEL_WATER_ALARM_NAME = "물주기 알람";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isPush = sharedPreferences.getBoolean("push", false);
        if (!isPush) {
            return;
        }

        NotificationChannel notificationChannel = getNotificationChannel();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Bundle bundle = intent.getExtras();
        int alarmId = getAlarmId(bundle);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_WATER_ALARM_ID);
        String name = intent.getStringExtra("name");
        builder
            .setContentText(name + " 물주기 알람입니다.")
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setSmallIcon(android.R.drawable.btn_star)
            .setContentIntent(pendingIntent);

        notificationManager.notify(alarmId, builder.build());
    }

    private NotificationChannel getNotificationChannel() {
        NotificationChannel notificationChannel =
            new NotificationChannel(NOTIFICATION_CHANNEL_WATER_ALARM_ID, NOTIFICATION_CHANNEL_WATER_ALARM_NAME,
                                    NotificationManager.IMPORTANCE_DEFAULT);

        notificationChannel.setDescription("물주기 알람");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[] {100, 200, 100, 200});
        return notificationChannel;
    }

    private int getAlarmId(Bundle bundle) {
        if (bundle == null) {
            return 0;
        }
        return bundle.getInt("alarmId");
    }
}
