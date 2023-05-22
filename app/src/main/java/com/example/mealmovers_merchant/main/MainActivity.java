package com.example.mealmovers_merchant.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mealmovers_merchant.R;
import com.example.mealmovers_merchant.databinding.ActivityMainBinding;
import com.example.mealmovers_merchant.main.viewModels.MainViewModel;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private MainViewModel mainViewModel;
    private final String CHANNEL_ID = "myChanelId";
    private final String CHANNEL_NAME = "OrderNotification";
    private final int NOTIFICATION_ID =  0;
    private PendingIntent pendingIntent;
    private Intent notificationIntent;
    private Boolean isDrawerOpen = false;
    private Boolean firstClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        openDrawer();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.init(this, binding);
        initNotification();
        createPendingIntent();

    }



    private void initNotification() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }




    private void createPendingIntent() {
        notificationIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
    }


    private void createNotification() {

        try {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID );
            builder.setContentTitle("New Order")
                    .setContentText("you have received new order")
                    .setSmallIcon(R.drawable.meal_movers_logo)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        }catch (Exception e){
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void sendNotificationOrderStatus(String status, String id){
        Map<String, String> notification = new HashMap<>();
        notification.put("order_id", id);
        notification.put("status", status);
//        notification.put("confirmationTime", selectedEstimationTime.getValue().toString() );

//        orderStatusCollectionRef.add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//            }
//        }).addOnFailureListener(e -> {
//            Log.e("notification", e.toString());
//            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        });


    }

    private void openDrawer() {
        binding.menuIconCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDrawerOpen) {
                    binding.drawerLayout.openDrawer(GravityCompat.END);
//                    if (firstClick){
//                        binding.xButton.animate().rotation(180.0f);
//                        Toast.makeText(MainActivity.this, "true", Toast.LENGTH_SHORT).show();
//                    }
//                    binding.menuIcon.animate().rotation(180.0f);

                    binding.menuIcon.animate().rotation(180.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.menuIcon.setVisibility(View.GONE);
                            binding.xButton.setVisibility(View.VISIBLE);
                            animation.removeAllListeners();

                        }
                    });

                    isDrawerOpen = true;
                }else{
                    firstClick = true;
                    binding.drawerLayout.closeDrawer(GravityCompat.END);
                    binding.xButton.setVisibility(View.GONE);
                    binding.menuIcon.setVisibility(View.VISIBLE);
                    binding.menuIcon.animate().rotation(-180.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.xButton.setVisibility(View.GONE);
                            binding.menuIcon.setVisibility(View.VISIBLE);
                        }
                    });
//                    binding.xButton.animate().rotation(-180.0f).setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//
//                            binding.xButton.setVisibility(View.GONE);
//                            binding.menuIcon.setVisibility(View.VISIBLE);
//                            animation.removeAllListeners();
//
//                        }
//                    });
                    isDrawerOpen = false;

                }
            }


        });

    }




}