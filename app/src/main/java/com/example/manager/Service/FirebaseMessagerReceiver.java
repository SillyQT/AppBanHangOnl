package com.example.manager.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.manager.R;
import com.example.manager.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagerReceiver extends FirebaseMessagingService {

    // 2001210289 - Huỳnh Công Huy - Bài 43: Nhận thông báo từ firebase
    // 2001210289 - Huỳnh Công Huy - Bài 44: Gửi và nhận thông báo trên app của user

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        if (message.getNotification() != null)
        {
            showNotification(message.getNotification().getTitle(), message.getNotification().getBody());
        }

    }

    private void showNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String channelId = "noti";
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.gcm_icon)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000 })
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        builder = builder.setContent(customView(title, body));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }

    private RemoteViews customView(String title, String body)
    {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title_noti, title);
        remoteViews.setTextViewText(R.id.body_noti, body);
        remoteViews.setImageViewResource(R.id.imgnoti, R.drawable.gcm_icon);
        return remoteViews;
    }

}
