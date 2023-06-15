package com.example.mealmovers_merchant.main.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.mealmovers_merchant.main.CallbackMethod;
import com.example.mealmovers_merchant.main.models.OrderModel;
import com.example.mealmovers_merchant.main.models.RestaurantModel;
import com.example.mealmovers_merchant.main.retrofit.RetrofitInstance;
import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersRepo {

    private static OrdersRepo instance;
    private final Context context;

    public OrdersRepo(Context context) {
        this.context = context;
    }

    public static OrdersRepo getInstance(Context context){
        if (instance == null){
            instance = new OrdersRepo(context);

        }
        return instance;
    }




    public void getAllNewOrders(String restaurantId,  CallbackMethod<List<OrderModel>> callback) throws InterruptedException {


        RetrofitInstance.ordersApi().getNewOrders(restaurantId).wait();
        RetrofitInstance.ordersApi().getNewOrders(restaurantId).enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                callback.onDone(response.body(), null);
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                callback.onDone(null, new Exception(t));
                Log.e("get all restaurants", t.getMessage() , new Exception(t));
            }
        });
    }



    public void updateOrderStatus(Context context, String orderId, String status){
        Call<OrderModel> call = RetrofitInstance.ordersApi().updateOrderStatus(orderId, status);

        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {

            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Toast.makeText(context, "couldn't updated status", Toast.LENGTH_SHORT).show();
                Log.e("update order status", t.getMessage() , new Exception(t));
            }
        });

    }



     public void getRestaurant(String id, CallbackMethod<RestaurantModel> callbackMethod){


          RetrofitInstance.restaurantApi().getRestaurant(id).enqueue(new Callback<RestaurantModel>() {
              @Override
              public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                  if (response.code() == 200){
                      callbackMethod.onDone(response.body(), null);
                  }
              }

              @Override
              public void onFailure(Call<RestaurantModel> call, Throwable t) {
                  Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                  Log.e("GetRestaurant", t.getMessage() , new Exception(t));
                  callbackMethod.onDone(null, new Exception(t));
              }
          });



     }








}
