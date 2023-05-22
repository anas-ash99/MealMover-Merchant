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
import com.example.mealmovers_merchant.main.repositories.OrdersRepo;
import com.example.mealmovers_merchant.main.use_cases.CreateNotification;
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

                    isDrawerOpen = false;

                }
            }


        });

    }




}