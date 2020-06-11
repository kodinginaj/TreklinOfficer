package com.example.treklinofficer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.treklinofficer.service.CheckNotificationService;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, CheckNotificationService.class);
        context.startService(i);
    }
}
