package com.yoonbae.planting.planner.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.yoonbae.planting.planner.MainActivity;
import com.yoonbae.planting.planner.R;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String NOTIFICATION_CHANNEL_WATER_ALARM_ID = "1";
    private static final String NOTIFICATION_CHANNEL_WATER_ALARM_NAME = "waterAlarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = getNotificationChannel();
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_WATER_ALARM_ID);

        Bundle bundle = intent.getExtras();
        int alarmId = 0;
        if (bundle != null) {
            alarmId = bundle.getInt("alarmId");
        }

        String name = intent.getStringExtra("name");
        builder.setContentTitle(context.getResources().getString(R.string.app_name))
            .setContentText(name + " 물주기 알람입니다.")
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(android.R.drawable.btn_star)
            .setContentIntent(pendingIntent);

        notificationManager.notify(alarmId, builder.build());
    }

    private NotificationChannel getNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(
                                                    NOTIFICATION_CHANNEL_WATER_ALARM_ID,
                                                    NOTIFICATION_CHANNEL_WATER_ALARM_NAME,
                                                    NotificationManager.IMPORTANCE_DEFAULT);

        notificationChannel.setDescription("water Alarm");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[] {100, 200, 100, 200});
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        return notificationChannel;
    }
}
