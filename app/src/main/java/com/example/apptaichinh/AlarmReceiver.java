package com.example.apptaichinh;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "ALARM_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        // Tạo intent để mở MainActivity khi nhấn vào thông báo
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0);

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Thông báo")
                .setContentText("Hãy nhập thu chi hôm nay nhé!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Hiển thị thông báo
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel(Context context) {
        // Tạo kênh thông báo cho các phiên bản Android Oreo trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Channel";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký kênh với hệ thống
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
