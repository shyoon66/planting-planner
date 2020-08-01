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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.yoonbae.planting.planner.MainActivity;
import com.yoonbae.planting.planner.R;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String NOTIFICATION_CHANNEL_WATER_ALARM_ID = "1";
    private static final String NOTIFICATION_CHANNEL_WATER_ALARM_NAME = "waterAlarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isPush = sharedPreferences.getBoolean("push", true);
        if (!isPush) {
            return;
        }

        boolean isVibration = sharedPreferences.getBoolean("vibration", true);
        NotificationChannel notificationChannel = getNotificationChannel(isVibration);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Bundle bundle = intent.getExtras();
        int alarmId = getAlarmId(bundle);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_WATER_ALARM_ID);
        String name = intent.getStringExtra("name");
        builder
            .setContentTitle(context.getResources().getString(R.string.app_name))
            .setContentText(name + " 물주기 알람입니다.")
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setSmallIcon(android.R.drawable.btn_star)
            .setContentIntent(pendingIntent);

        boolean isSound = sharedPreferences.getBoolean("sound", true);
        builder.setSound(getSound(isSound));
        notificationManager.notify(alarmId, builder.build());
    }

    private NotificationChannel getNotificationChannel(boolean isVibration) {
        NotificationChannel notificationChannel =
            new NotificationChannel(NOTIFICATION_CHANNEL_WATER_ALARM_ID, NOTIFICATION_CHANNEL_WATER_ALARM_NAME,
                                    NotificationManager.IMPORTANCE_DEFAULT);

        notificationChannel.setDescription("water Alarm");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);

        if (isVibration) {
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[] {100, 200, 100, 200});
        } else {
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(null);
        }
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        return notificationChannel;
    }

    private Uri getSound(boolean isSound) {
        if (isSound) {
            return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        return null;
    }

    private int getAlarmId(Bundle bundle) {
        if (bundle == null) {
            return 0;
        }
        return bundle.getInt("alarmId");
    }
}
