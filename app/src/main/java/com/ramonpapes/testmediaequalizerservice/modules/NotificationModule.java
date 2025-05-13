package com.ramonpapes.testmediaequalizerservice.modules;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ramonpapes.testmediaequalizerservice.MainActivity;
import com.ramonpapes.testmediaequalizerservice.R;

public class NotificationModule {
    private static final String CHANNEL_ID = "AudioServiceChannel";

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Audio Service", NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public static Notification buildNotification(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        );

        return new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle("Reproduzindo Áudio")
                .setContentText("Seu som está tocando")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
    }
}
