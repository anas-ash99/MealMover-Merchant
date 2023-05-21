package com.example.mealmovers_merchant.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealmovers_merchant.R;
import com.example.mealmovers_merchant.databinding.ActivityMainBinding;
import com.example.mealmovers_merchant.main.adapters.ClicksInterFaceOrdersRV;
import com.example.mealmovers_merchant.main.adapters.NewOrderItem;
import com.example.mealmovers_merchant.main.adapters.OrderItemAdapter;
import com.example.mealmovers_merchant.main.dialogs.DialogOrder;
import com.example.mealmovers_merchant.main.use_cases.FireBaseCase;
import com.example.mealmovers_merchant.main.models.OrderModel;
import com.example.mealmovers_merchant.main.repositories.OrdersRepo;
import com.example.mealmovers_merchant.main.viewModels.MainViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ClicksInterFaceOrdersRV {


    private ActivityMainBinding binding;
    MainViewModel mainViewModel;
    List<OrderModel> newOrders = new ArrayList<>();
    MutableLiveData<Integer> selectedEstimationTime = new MutableLiveData<>(0);
    FirebaseFirestore db  = FirebaseFirestore.getInstance();
    CollectionReference collectionRef = db.collection("orders");
    CollectionReference orderStatusCollectionRef = db.collection("order status");
    NewOrderItem adapter;
    OrderModel order;
    String orderId;
    OrderItemAdapter newOrderItemAdapter, kitchenOrderItemAdapter, beingDeliveredItemAdapter, deliveredItemAdapter;
    OrdersRepo ordersRepo;
    Dialog dialog;
    CardView thirtyMinCard, fortyMinCard, sixtyMinCard, eightyMinCard, hundredMinCard, confirmButton;
    TextView thirtyMinTV, fortyMinTV, sixtyMinTV, eightyMinTV,hundredMinTV;
    TextView deliveryTime, paymentStatus, totalPrice, phoneNumber, deliveryType, addressLine2, addressLine1, customerName;
    RecyclerView orderMenuItemsRV;
    MutableLiveData<Integer> newOrdersCount = new MutableLiveData<>(0);
    MutableLiveData<Integer> kitchenOrderCount = new MutableLiveData<>(0);
    MutableLiveData<Integer> beingDeliveredCount = new MutableLiveData<>(0);
    MutableLiveData<Integer> deliveredCount = new MutableLiveData<>(0);
    DialogOrder orderDialog;
    String CHANNEL_ID = "myChanelId";
    String CHANNEL_NAME = "OrderNotification";
    int NOTIFICATION_ID =  0;
    PendingIntent pendingIntent;
    Intent notificationIntent;
    LocalDateTime deliverTime = LocalDateTime.now();
    Boolean isDrawerOpen = false;
    Boolean firstClick = false;
    int renderCount = 0;
    MediaPlayer mediaPlayer;



    private FireBaseCase fireBaseCase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        handlePageRefresh();
        openDrawer();
        ordersRepo = OrdersRepo.getInstance();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.init();
        orderDialog = new DialogOrder(this);
        fireBaseCase = new FireBaseCase(this);
        initNewOrderDialog();
        handleArrowBackwardForward();
        observeOrdersLoading();
        observeOrdersCount();
        initNotification();

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.mewe);

        createPendingIntent();

        renderCount += renderCount;



    }



    private void initNotification() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog.isShowing()){
            dialog.hide();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog.isShowing()){
            dialog.hide();
            selectedEstimationTime.setValue(0);
        }
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

    private void observeOrdersCount() {
        newOrdersCount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.newTV.setText("NEW (" + integer + ")");
            }
        });

        kitchenOrderCount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.kitchenTV.setText("KITCHEN (" + integer + ")");
            }
        });

        beingDeliveredCount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.beingDeliveredTV.setText("BEING DELIVERED (" + integer + ")");
            }
        });

        deliveredCount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.deliveredTV.setText("DELIVERED (" + integer + ")");
            }
        });



    }

    private void observeOrdersLoading() {
       ordersRepo.getNewOrdersLoading().observe(this, new Observer<String>() {
           @Override
           public void onChanged(String s) {
               if (s.equals("done")){
//                   if (mainViewModel.getTodaysOrder().isEmpty()) {
                       mainViewModel.setTodaysOrder(ordersRepo.getNewOrders());
                       initNewOrdersRecyclerView();
                       initBeingDeliveredOrdersRV();
                       initKitchenOrdersRV();
                       initDeliveredOrdersRV();
                       initNotConfirmedOrders();
                       kitchenOrderCount.setValue(mainViewModel.getKitchenOrders().size());
                       newOrdersCount.setValue(mainViewModel.getNewOrders().size());
                       beingDeliveredCount.setValue(mainViewModel.getBeingDeliveredOrders().size());
                       deliveredCount.setValue(mainViewModel.getDeliveredOrders().size());
                      Log.v("size", ordersRepo.getNewOrders().size() + "");
//                   }

               }else if (s.equals("error")){

                   Toast.makeText(MainActivity.this, "network error", Toast.LENGTH_SHORT).show();
               }
               binding.swipeRefreshLayout.setRefreshing(false);
           }
       });
    }

    private void initNotConfirmedOrders() {
        for (OrderModel order: mainViewModel.getNotConfirmedOrders()) {
            this.order = order;
            showNewOrderDialog();
            initNewOrder();
            break;
        }

    }

    private void handleArrowBackwardForward() {

//        binding.orderLayout.arrowBackward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.orderLayout.horizontalScrollView.arrowScroll(1);
//            }
//        });
//
//        binding.orderLayout.arrowForward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.orderLayout.horizontalScrollView.computeScroll();
//            }
//        });
    }

    private void selectEstimationTime() {
        selectedEstimationTime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (selectedEstimationTime.getValue()){
                    case 0:
                        thirtyMinCard.setCardBackgroundColor(getColor(R.color.white));
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(getColor(R.color.secondary_background));
                        break;
                    case 30:
//                        thirtyMinTV.setTextColor(getColor(R.color.white));
                        thirtyMinCard.setCardBackgroundColor(getColor(R.color.teal_200));
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(getColor(R.color.teal_200));
                        break;
                    case 40:
                        fortyMinCard.setCardBackgroundColor(getColor(R.color.teal_200));
                        thirtyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(getColor(R.color.teal_200));
                        break;
                    case 60:
                        sixtyMinCard.setCardBackgroundColor(getColor(R.color.teal_200));
                        thirtyMinCard.setCardBackgroundColor(Color.WHITE);
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(getColor(R.color.teal_200));
                        break;

                    case 80:
                        eightyMinCard.setCardBackgroundColor(getColor(R.color.teal_200));
                        thirtyMinCard.setCardBackgroundColor(Color.WHITE);
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(getColor(R.color.teal_200));
                        break;

                    case 100:
                        hundredMinCard.setCardBackgroundColor(getColor(R.color.teal_200));
                        thirtyMinCard.setCardBackgroundColor(Color.WHITE);
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(getColor(R.color.teal_200));
                        break;

                }

            }
        });


        thirtyMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliverTime = LocalDateTime.now().plusMinutes(30);
                selectedEstimationTime.setValue(30);
            }
        });

        fortyMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEstimationTime.setValue(40);
                deliverTime = LocalDateTime.now().plusMinutes(40);
            }
        });

        sixtyMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEstimationTime.setValue(60);
                deliverTime = LocalDateTime.now().plusMinutes(60);
            }
        });
        eightyMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEstimationTime.setValue(80);
                deliverTime = LocalDateTime.now().plusMinutes(80);
            }
        });
        hundredMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliverTime = LocalDateTime.now().plusMinutes(100);
                selectedEstimationTime.setValue(100);
            }
        });


    }

    public void handleConfirmOrderClick() {

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEstimationTime.getValue() != 0 || !order.getDeliveryTime().equals("As soon as possible")){
                    order.setStatus("confirmed");
//                    sendNotificationOrderStatus("confirmed", order.get_id());
                    mainViewModel.addOrder(order);
                    initNewOrdersRecyclerView();
                    newOrdersCount.setValue(newOrdersCount.getValue() + 1);
                    if (order.getScheduled()){
                        dialog.findViewById(R.id.timeHeaderTv).setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.timeList).setVisibility(View.VISIBLE);
                        confirmButton.setCardBackgroundColor(getColor(R.color.secondary_background));
                    }
                    dialog.hide();
                    mainViewModel.getNotConfirmedOrders().remove(order);
                    ordersRepo.updateOrderStatus(getApplication(), order.get_id(), "confirmed");
                    selectedEstimationTime.setValue(0);
                    initNotConfirmedOrders();

                }else {
                    Toast.makeText(MainActivity.this, "Please choose a time", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void sendNotificationOrderStatus(String status, String id){
        Map<String, String> notification = new HashMap<>();
        notification.put("order_id", id);
        notification.put("status", status);
        notification.put("confirmationTime", selectedEstimationTime.getValue().toString() );

        orderStatusCollectionRef.add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("notification", e.toString());
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void listingToNewOrders() {
        try {
            collectionRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot orders, @Nullable FirebaseFirestoreException error) {

                    if (error != null){
                        Log.e("fireStore", error.getMessage() );
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if (orders != null) {
                        for (QueryDocumentSnapshot value : orders) {
                            try {
                                if (value.get("restaurant_id").equals("641a35f33f50168a64ca2f68") && value.get("status").equals("new")) {
                                    order = value.toObject(OrderModel.class);
                                    mainViewModel.getNotConfirmedOrders().add(0, order);
                                    initNotConfirmedOrders();
                                    orderId = value.getId();
                                    db.collection("orders").document(orderId).delete();
                                    ordersRepo.updateOrderStatus(getApplication(), order.get_id(), "received");
                                    db.collection("orders").document(orderId).update("status", "received");
//                                    createNotification();
                                    mediaPlayer.start();
                                    break;
                                }

                            }catch (Exception e){
                                Log.e("order line 332", e.toString());
                                Toast.makeText(MainActivity.this, "line 264" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }catch (Exception e){
             Log.e("new order", e.toString());

        }



    }


    private void initNewOrderDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        confirmButton = dialog.findViewById(R.id.confirm_button);
        thirtyMinCard = dialog.findViewById(R.id.thirtyMinCard);
        fortyMinCard = dialog.findViewById(R.id.fortyMinCard);
        sixtyMinCard = dialog.findViewById(R.id.sixtyMinCard);
        eightyMinCard = dialog.findViewById(R.id.eightyMinCard);
        hundredMinCard = dialog.findViewById(R.id.hundredMinCard);
        orderMenuItemsRV = dialog.findViewById(R.id.itemsRecyclerView);
        deliveryTime = dialog.findViewById(R.id.deliveryTime);
        paymentStatus = dialog.findViewById(R.id.paymentStatus);
        totalPrice = dialog.findViewById(R.id.totalPrice);
        deliveryType = dialog.findViewById(R.id.deliveryType);
        phoneNumber = dialog.findViewById(R.id.phoneNumber);
        addressLine2 = dialog.findViewById(R.id.addressLine2);
        addressLine1 = dialog.findViewById(R.id.addressLine1);
        customerName = dialog.findViewById(R.id.customerName);
        thirtyMinTV = dialog.findViewById(R.id.thirtyMinTV);
        dialog.setCancelable(false);

    }

    private void showNewOrderDialog() {

        if (dialog.isShowing()){
            dialog.hide();
        }
        initOrderItemsRV();
        selectEstimationTime();
        handleConfirmOrderClick();
        if (order.getScheduled().equals(true)){
             dialog.findViewById(R.id.timeHeaderTv).setVisibility(View.GONE);
             dialog.findViewById(R.id.timeList).setVisibility(View.GONE);
             confirmButton.setCardBackgroundColor(getColor(R.color.teal_200));
        }else{
            dialog.findViewById(R.id.timeHeaderTv).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.timeList).setVisibility(View.VISIBLE);
            confirmButton.setCardBackgroundColor(getColor(R.color.secondary_background));
        }

        dialog.show();

    }



   public void initOrderItemsRV() {
        adapter = new NewOrderItem(this, order.getItems());
        orderMenuItemsRV.setAdapter(adapter);
        orderMenuItemsRV.setLayoutManager(new LinearLayoutManager(this));
    }

    public void initKitchenOrdersRV() {

        if (!mainViewModel.getKitchenOrders().isEmpty()) {
            kitchenOrderItemAdapter = new OrderItemAdapter(this, mainViewModel.getKitchenOrders(), this);
            binding.kitchenOrdersRV.setAdapter(kitchenOrderItemAdapter);
            binding.kitchenOrdersRV.setLayoutManager(new LinearLayoutManager(this));
            binding.kitchenOrdersRV.setVisibility(View.VISIBLE);
            binding.kitchenOrdersCard.setVisibility(View.GONE);
        }else {
            binding.kitchenOrdersRV.setVisibility(View.GONE);
            binding.kitchenOrdersCard.setVisibility(View.VISIBLE);
        }
    }

    public void initBeingDeliveredOrdersRV() {
        if (!mainViewModel.getBeingDeliveredOrders().isEmpty()) {
            beingDeliveredItemAdapter = new OrderItemAdapter(this, mainViewModel.getBeingDeliveredOrders(), this);
            binding.beingDeliveredOrdersRV.setAdapter(beingDeliveredItemAdapter);
            binding.beingDeliveredOrdersRV.setLayoutManager(new LinearLayoutManager(this));
            binding.beingDeliveredOrdersRV.setVisibility(View.VISIBLE);
            binding.beingDeliveredCard.setVisibility(View.GONE);
        }else {
            binding.beingDeliveredOrdersRV.setVisibility(View.GONE);
            binding.beingDeliveredCard.setVisibility(View.VISIBLE);
        }
    }
    private void initDeliveredOrdersRV() {
        if (!mainViewModel.getDeliveredOrders().isEmpty()) {
            deliveredItemAdapter = new OrderItemAdapter(this, mainViewModel.getDeliveredOrders(),this);
            binding.deliveredOrdersRV.setAdapter(deliveredItemAdapter);
            binding.deliveredOrdersRV.setLayoutManager(new LinearLayoutManager(this));
            binding.deliveredOrdersRV.setVisibility(View.VISIBLE);
            binding.deliveredOrdersCard.setVisibility(View.GONE);
        }else {
            binding.deliveredOrdersRV.setVisibility(View.GONE);
            binding.deliveredOrdersCard.setVisibility(View.VISIBLE);
        }
    }


    private void initNewOrder() {
        customerName.setText(order.getAddress().getName());
        addressLine1.setText(order.getAddress().getStreetName()+ " " + order.getAddress().getHouseNumber() );
        addressLine2.setText(order.getAddress().getZipCode() + " " + order.getAddress().getCity());
        phoneNumber.setText(order.getAddress().getPhoneNumber());
        deliveryType.setText(order.getType().toUpperCase());
        totalPrice.setText(order.getOrderPrice() + "€");
        paymentStatus.setText(order.getPaymentStatus());
        if (order.getScheduled().equals(false)){
            deliveryTime.setText("ASAP");
        }else {
            deliveryTime.setText(order.getDeliveryTime());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        listingToNewOrders();
        ordersRepo.setNewOrders("641a35f33f50168a64ca2f68");
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

   public void initNewOrdersRecyclerView() {

       if (!mainViewModel.getNewOrders().isEmpty()){
           Toast.makeText(this, "new hii", Toast.LENGTH_SHORT).show();
           newOrderItemAdapter = new OrderItemAdapter(this, mainViewModel.getNewOrders(),this);
           binding.newOrdersRV.setAdapter(newOrderItemAdapter);
           binding.newOrdersRV.setLayoutManager(new LinearLayoutManager(this));
           binding.newOrdersRV.setVisibility(View.VISIBLE);
           binding.newCard.setVisibility(View.GONE);
           Log.v("order","truee");
       }else {
           binding.newOrdersRV.setVisibility(View.GONE);
           binding.newCard.setVisibility(View.VISIBLE);
           Log.v("order","false");
       }
   }
    private void handlePageRefresh() {
       binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mainViewModel.init("641a35f33f50168a64ca2f68");
//                mainViewModel.setTodaysOrderToNull();
//                ordersRepo.setNewOrders("641a35f33f50168a64ca2f68");
//                List<OrderModel> emptyList = new ArrayList<>();
//                mainViewModel.setNewOrders(emptyList);
                createNotification();
                binding.swipeRefreshLayout.setRefreshing(false);
//                mainViewModel.setKitchenOrders(emptyList);


            }
        });
    }


    @Override
    public void onMoveOrderClick(OrderModel orderModel) {
//       mainViewModel.handleOrderClick(orderModel);

        try {
            switch (orderModel.getStatus()){
                case "confirmed":
                    orderModel.setStatus("kitchen");
                    mainViewModel.getNewOrders().remove(orderModel);
                    mainViewModel.getKitchenOrders().add(0, orderModel);
                    newOrdersCount.setValue(newOrdersCount.getValue() - 1);
                    kitchenOrderCount.setValue(kitchenOrderCount.getValue() + 1);
                    ordersRepo.updateOrderStatus(getApplication(), orderModel.get_id(), "kitchen");
                    sendNotificationOrderStatus("kitchen", orderModel.get_id());

//                    newOrderItemAdapter.addOrderToList(orderModel);
//                    kitchenOrderItemAdapter.notifyDataSetChanged();
                    break;
                case "kitchen":
                    orderModel.setStatus("being delivered");
                    mainViewModel.getKitchenOrders().remove(orderModel);
                    mainViewModel.getBeingDeliveredOrders().add(0, orderModel);
                    kitchenOrderCount.setValue(kitchenOrderCount.getValue() - 1);
                    beingDeliveredCount.setValue(beingDeliveredCount.getValue() + 1);
                    ordersRepo.updateOrderStatus(getApplication(), orderModel.get_id(), "being delivered");
                    sendNotificationOrderStatus("being delivered", orderModel.get_id());
                    break;

                case "being delivered":
                    orderModel.setStatus("delivered");
                    mainViewModel.getBeingDeliveredOrders().remove(orderModel);
                    mainViewModel.getDeliveredOrders().add(0, orderModel);
                    deliveredCount.setValue(deliveredCount.getValue() + 1);
                    beingDeliveredCount.setValue(beingDeliveredCount.getValue() - 1);
                    ordersRepo.updateOrderStatus(getApplication(), orderModel.get_id(), "delivered");
                    sendNotificationOrderStatus("delivered", orderModel.get_id());
                    break;


            }
            initNewOrdersRecyclerView();
            initKitchenOrdersRV();
            initBeingDeliveredOrdersRV();
            initDeliveredOrdersRV();
        }catch (Exception e){
            Log.e("tag", e.toString());
        }

    }
}