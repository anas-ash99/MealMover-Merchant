package com.example.mealmovers_merchant.main.use_cases;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mealmovers_merchant.R;
import com.example.mealmovers_merchant.main.MainActivity;

public class CreateNotification {

    private  static final String CHANNEL_ID = "myChanelId";
    private static final String CHANNEL_NAME = "OrderNotification";
    private static final int NOTIFICATION_ID =  0;
    private static PendingIntent pendingIntent;
    private static Intent notificationIntent;

    private static void initNotification(Context context) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    private static void createPendingIntent(Context context) {
        notificationIntent = new Intent(context, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
    }

    public static void createNotification(Context context) {

        try {

            initNotification(context);
            createPendingIntent(context);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID );
            builder.setContentTitle("New Order")
                    .setContentText("you have received new order")
                    .setSmallIcon(R.drawable.meal_movers_logo)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        }catch (Exception e){
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



}
