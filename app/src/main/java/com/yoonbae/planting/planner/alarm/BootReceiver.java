package com.yoonbae.planting.planner.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!"android.intent.action.BOOT_COMPLETED".equals(action)) {
            return;
        }
        Intent waterAlarmIntent = new Intent(context, BootWaterAlarm.class);
        waterAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(waterAlarmIntent);
    }
}
