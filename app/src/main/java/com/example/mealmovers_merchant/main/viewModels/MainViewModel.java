package com.example.mealmovers_merchant.main.viewModels;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.mealmovers_merchant.R;
import com.example.mealmovers_merchant.databinding.ActivityMainBinding;
import com.example.mealmovers_merchant.main.DataHolder;
import com.example.mealmovers_merchant.main.ExtensionMethods;
import com.example.mealmovers_merchant.main.MainActivity;
import com.example.mealmovers_merchant.main.adapters.OrderItemAdapter;
import com.example.mealmovers_merchant.main.enum_classes.OrderStatus;
import com.example.mealmovers_merchant.main.models.OrderModel;
import com.example.mealmovers_merchant.main.models.RestaurantModel;
import com.example.mealmovers_merchant.main.repositories.OrdersRepo;
import com.example.mealmovers_merchant.main.use_cases.CreateNotification;
import com.example.mealmovers_merchant.main.use_cases.dialogs.DialogOrder;
import com.example.mealmovers_merchant.main.use_cases.firebase_case.FireBaseCase;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class MainViewModel extends ViewModel {

    List<OrderModel> todaysOrder = new ArrayList<>();
    List<OrderModel> newOrders = new ArrayList<>();
    List<OrderModel> kitchenOrders = new ArrayList<>();
    List<OrderModel> beingDeliveredOrders = new ArrayList<>();
    List<OrderModel> deliveredOrders = new ArrayList<>();
    List<OrderModel> notConfirmedOrders = new ArrayList<>();
    Boolean isPageInited = false;
    private MainActivity activity;
    private ActivityMainBinding binding;
    private OrdersRepo repo;
    private RestaurantModel restaurant;
    private FireBaseCase fireBaseCase;
    private MediaPlayer mediaPlayer;
    private DialogOrder orderDialog;
    private final String restaurant_id = "641a35f33f50168a64ca2f68";


    public void init(MainActivity activity, ActivityMainBinding binding){
        this.binding = binding;
        this.activity = activity;
        fireBaseCase = new FireBaseCase(activity);
        repo = OrdersRepo.getInstance(activity);
        orderDialog = new DialogOrder(activity, this);
        fetchAllOrders();
        getRestaurant();
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(activity, R.raw.mewe);
        handlePageRefresh();

        isPageInited = true;
    }

    private void getRestaurant() {


        repo.getRestaurant(restaurant_id,(results, error) ->{
           if (results != null){
              this.restaurant = results;
              binding.setRestaurant(restaurant);
              DataHolder.setLoggedInRestaurant(restaurant);

           }
        });
    }

    public void observeNewOrdersFirebase() {
        try {
        fireBaseCase.listingToNewOrders(restaurant_id, results -> {
//            orderDialog.dialog.hide();

            if (isPageInited && results.size() != 0){
                mediaPlayer.start();
                CreateNotification.createNotification(activity);
            }
             notConfirmedOrders.removeAll(notConfirmedOrders);
             notConfirmedOrders = results;
             showNewOrderDialog();
            showNewOrderDialog();
        });

        }catch (Exception e){
            Log.e("order", e.toString());

        }
    }




    public void fetchAllOrders(){
        try {

            repo.getAllNewOrders("641a35f33f50168a64ca2f68",  (results, error) -> {
                if (error != null){
                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (results != null){
                    todaysOrder = results;
                    setTodaysOrder();
                    binding.swipeRefreshLayout.setRefreshing(false);
                }

            });
        }catch (Exception e){

        }
    }


    private void showNewOrderDialog(){
        System.out.println(notConfirmedOrders.size());
        if (notConfirmedOrders.size() > 0){
            orderDialog.showNewOrderDialog(notConfirmedOrders.get(0));

        }else {
            orderDialog.dialog.hide();
        }
    }

    public void setTodaysOrder() {
        newOrders.removeAll(newOrders);
        kitchenOrders.removeAll(kitchenOrders);
        beingDeliveredOrders.removeAll(beingDeliveredOrders);
        deliveredOrders.removeAll(deliveredOrders);
        for (OrderModel order : todaysOrder) {
            switch (order.getStatus()) {
                case "confirmed":
                    newOrders.add(order);
//                    System.out.println("neworders: " + newOrders.size());

                    break;
                case "kitchen":
                    kitchenOrders.add(order);
//                    System.out.println("kitchenOrders: " + newOrders.size());
                    initNewOrdersRecyclerView(OrderStatus.KITCHEN);
                    break;
                case "being_delivered":
                    beingDeliveredOrders.add(order);
//                    System.out.println("beingDeliveredOrders: " + newOrders.size());
                    initNewOrdersRecyclerView(OrderStatus.BEING_DELIVERED);
                    break;
                case "delivered":
                    deliveredOrders.add(order);
//                    System.out.println("deliveredOrders: " + newOrders.size());
                    initNewOrdersRecyclerView(OrderStatus.DELIVERED);
                    break;

            }

        }

        initNewOrdersRecyclerView(OrderStatus.NEW);
    }


    public void onConfirmOrderClick(){

        if (notConfirmedOrders.size() > 0){
            fireBaseCase.deleteItem(notConfirmedOrders.get(0).getOrderFireStoreId());
            repo.updateOrderStatus(activity,notConfirmedOrders.get(0).get_id(), "confirmed");
            notConfirmedOrders.get(0).setStatus("confirmed");
            newOrders.add(0, notConfirmedOrders.get(0));
            notConfirmedOrders.remove(0);
            initNewOrdersRecyclerView(OrderStatus.NEW);
        }

    }
    public void handleMoveOrderClick(OrderModel order){
       switch (order.getStatus()){
           case "confirmed":
               order.setStatus("kitchen");
               repo.updateOrderStatus(activity, order.get_id(), "kitchen");
               newOrders.remove(order);
               kitchenOrders.add(0,order);
               break;
           case "kitchen":
               order.setStatus("being_delivered");
               repo.updateOrderStatus(activity, order.get_id(), "being_delivered");
               this.kitchenOrders.remove(order);
               this.beingDeliveredOrders.add(0,order);
               break;
           case "being_delivered":
               order.setStatus("delivered");

               repo.updateOrderStatus(activity, order.get_id(), "delivered");
               beingDeliveredOrders.remove(order);
               deliveredOrders.add(0,order);

               break;
       }
        initNewOrdersRecyclerView(OrderStatus.NEW);
        initNewOrdersRecyclerView(OrderStatus.KITCHEN);
        initNewOrdersRecyclerView(OrderStatus.BEING_DELIVERED);
        initNewOrdersRecyclerView(OrderStatus.DELIVERED);


    }




    @SuppressLint("SetTextI18n")
    public void initNewOrdersRecyclerView(OrderStatus orderStatus) {
        
        switch (orderStatus){
            case NEW:

                if (!newOrders.isEmpty()){
                    OrderItemAdapter itemsAdapter = new OrderItemAdapter(activity,newOrders, this);
                    binding.newOrdersRV.setAdapter(itemsAdapter);
                    binding.newOrdersRV.setLayoutManager(new LinearLayoutManager(activity));
                    binding.newOrdersRV.setVisibility(View.VISIBLE);
                    binding.newCard.setVisibility(View.GONE);

                }else {
                    binding.newOrdersRV.setVisibility(View.GONE);
                    binding.newCard.setVisibility(View.VISIBLE);
                }
                binding.newTV.setText("NEW (" + newOrders.size() +  ")");
                break;
            case KITCHEN:
                if (!kitchenOrders.isEmpty()) {
                    OrderItemAdapter itemsAdapter = new OrderItemAdapter(activity,kitchenOrders, this);

                    binding.kitchenOrdersRV.setAdapter(itemsAdapter);
                    binding.kitchenOrdersRV.setLayoutManager(new LinearLayoutManager(activity));
                    binding.kitchenOrdersRV.setVisibility(View.VISIBLE);
                    binding.kitchenOrdersCard.setVisibility(View.GONE);

                }else {
                    binding.kitchenOrdersRV.setVisibility(View.GONE);
                    binding.kitchenOrdersCard.setVisibility(View.VISIBLE);
                }
                binding.kitchenTV.setText("KITCHEN (" + kitchenOrders.size() +  ")");
                break;
            case BEING_DELIVERED:

                if (!beingDeliveredOrders.isEmpty()) {
                    OrderItemAdapter itemsAdapter = new OrderItemAdapter(activity,beingDeliveredOrders, this);
                    binding.beingDeliveredOrdersRV.setAdapter(itemsAdapter);
                    binding.beingDeliveredOrdersRV.setLayoutManager(new LinearLayoutManager(activity));
                    binding.beingDeliveredOrdersRV.setVisibility(View.VISIBLE);
                    binding.beingDeliveredCard.setVisibility(View.GONE);

                }else {
                    binding.beingDeliveredOrdersRV.setVisibility(View.GONE);
                    binding.beingDeliveredCard.setVisibility(View.VISIBLE);
                }
                binding.beingDeliveredTV.setText("BEING DELIVERED(" + beingDeliveredOrders.size() +  ")");

            case DELIVERED:

                if (!deliveredOrders.isEmpty()) {
                    OrderItemAdapter itemsAdapter = new OrderItemAdapter(activity,deliveredOrders, this);
                    binding.deliveredOrdersRV.setAdapter(itemsAdapter);
                    binding.deliveredOrdersRV.setLayoutManager(new LinearLayoutManager(activity));
                    binding.deliveredOrdersRV.setVisibility(View.VISIBLE);
                    binding.deliveredOrdersCard.setVisibility(View.GONE);

                }else {
                    binding.deliveredOrdersRV.setVisibility(View.GONE);
                    binding.deliveredOrdersCard.setVisibility(View.VISIBLE);
                }
                binding.deliveredTV.setText("DELIVERED (" + deliveredOrders.size() +  ")");

        }






    }




    private void handlePageRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchAllOrders();
        });
    }


}




