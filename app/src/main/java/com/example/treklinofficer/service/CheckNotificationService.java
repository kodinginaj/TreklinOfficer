package com.example.treklinofficer.service;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.treklinofficer.MainActivity;
import com.example.treklinofficer.R;
import com.example.treklinofficer.api.ApiRequest;
import com.example.treklinofficer.api.Retroserver;
import com.example.treklinofficer.model.ResponseModel;
import com.example.treklinofficer.util.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckNotificationService extends Service {

    public CheckNotificationService() {
        super();
    }
    public static final long syncInterval = 10000;
//    SimpleDateFormat simpleDateFormat;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, CheckNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(CheckNotificationService.this, 1, alarmIntent, 0);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + syncInterval , pendingIntent);
        }

        Log.d("Nofitikasi", "CheckNotif");

        tampilNotif();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void tampilNotif(){
        Session session = new Session(CheckNotificationService.this);

        ApiRequest api = Retroserver.getClient().create(ApiRequest.class);
        Call<ResponseModel> updateLocation = api.updateLocation(session.getId(),session.getLatitude(),session.getLongitude());
        updateLocation.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(CheckNotificationService.this, "Update Teros", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(CheckNotificationService.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


