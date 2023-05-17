package com.example.mealmovers_merchant.main.repositories;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.mealmovers_merchant.main.models.OrderModel;
import com.example.mealmovers_merchant.main.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersRepo {


    private static OrdersRepo instance;
    MutableLiveData<String>  newOrdersLoading = new MutableLiveData<>();
    List<OrderModel> newOrders = new ArrayList<>();




    public static OrdersRepo getInstance(){
        if (instance == null){
            instance = new OrdersRepo();
        }
        return instance;
    }

    public List<OrderModel> getNewOrders() {
        return newOrders;
    }

    public void setNewOrders(String resId) {
        Call<List<OrderModel>> call = RetrofitInstance.ordersApi().getNewOrders(resId);
        call.enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                List<OrderModel> emptyList = new ArrayList<>();
                newOrders = emptyList;
                Log.v("order", response.body().size()+ "");
                if (response.code() == 200){
                    newOrders = response.body();
                    newOrdersLoading.setValue("done");
                }else {
                    newOrdersLoading.setValue("error");
                    Log.e("order",response.message() );
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                newOrdersLoading.setValue("error");
                Log.e("new orders", t.toString());

            }
        });
    }

    public MutableLiveData<String> getNewOrdersLoading() {
        return newOrdersLoading;
    }

    public void setNewOrdersLoading(String newOrdersLoading) {
        this.newOrdersLoading.setValue(newOrdersLoading);
    }

    public void updateOrderStatus(Application application, String orderId, String status){
        Call<OrderModel> call = RetrofitInstance.ordersApi().updateOrderStatus(orderId, status);

        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {

            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Toast.makeText(application, "couldn't updated status", Toast.LENGTH_SHORT).show();
                Log.e("status", t.toString());
            }
        });

    }







}
